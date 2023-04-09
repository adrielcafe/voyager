/*
* Copyright 2014-2021 JetBrains s.r.o and contributors. Use of this source code is governed by the Apache 2.0 license.
*/

package cafe.adriel.voyager.routing.core.routing

import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.routing.core.application.Application
import cafe.adriel.voyager.routing.core.application.ApplicationCall
import cafe.adriel.voyager.routing.core.application.ApplicationEnvironment
import cafe.adriel.voyager.routing.core.application.BaseApplicationPlugin
import cafe.adriel.voyager.routing.core.application.call
import cafe.adriel.voyager.routing.core.application.install
import cafe.adriel.voyager.routing.core.application.log
import cafe.adriel.voyager.routing.core.plugins.MissingRequestParameterException
import cafe.adriel.voyager.routing.core.plugins.RouteNotFoundException
import cafe.adriel.voyager.routing.core.plugins.TooManyRedirectException
import cafe.adriel.voyager.routing.core.screens.EmptyScreen
import io.ktor.http.Parameters
import io.ktor.http.plus
import io.ktor.util.AttributeKey
import io.ktor.util.KtorDsl
import io.ktor.util.logging.KtorSimpleLogger
import io.ktor.util.logging.Logger
import io.ktor.util.logging.isTraceEnabled
import io.ktor.util.pipeline.PipelineContext
import io.ktor.util.pipeline.execute
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

/**
 * A root routing node.
 * You can learn more about routing in Ktor from [Routing](https://ktor.io/docs/routing-in-ktor.html).
 */
@KtorDsl
public class VoyagerRouting internal constructor(
    internal val application: Application,
) : VoyagerRoute(
    parent = null,
    selector = RootRouteSelector(""),
    application.environment.developmentMode,
    application.environment
) {
    private val tracers = mutableListOf<(RoutingResolveTrace) -> Unit>()
    private val namedRoutes = mutableMapOf<String, VoyagerRoute>()
    internal val navigation = MutableStateFlow<VoyagerRouteEvent>(VoyagerRouteEvent.Idle)

    init {
        addDefaultTracing()
    }

    public val canPop: Boolean
        get() {
            val currentEvent = navigation.value
            return currentEvent is VoyagerRouteEvent.Idle && currentEvent.canPop
        }

    public fun pop() {
        executeCall(VoyagerApplicationCall(application = application, route = this))
    }

    public fun popUntil(predicate: (Screen) -> Boolean) {
        executeCall(VoyagerApplicationCall(application = application, route = this, popUntilPredicate = predicate))
    }

    public fun push(path: String, parameters: Parameters = Parameters.Empty) {
        executeCall(
            VoyagerApplicationCall(
                application = application,
                route = this,
                uri = path,
                parameters = parameters,
            )
        )
    }

    public fun pushNamed(
        name: String,
        parameters: Parameters = Parameters.Empty,
        pathReplacements: Parameters = Parameters.Empty,
    ) {
        executeCall(
            VoyagerApplicationCall(
                application = application,
                route = this,
                name = name,
                parameters = parameters,
                pathReplacements = pathReplacements,
            )
        )
    }

    public fun replace(path: String, parameters: Parameters = Parameters.Empty) {
        executeCall(
            VoyagerApplicationCall(
                application = application,
                route = this,
                uri = path,
                parameters = parameters,
                replaceCurrent = true,
            )
        )
    }

    public fun replaceAll(path: String, parameters: Parameters = Parameters.Empty) {
        executeCall(
            VoyagerApplicationCall(
                application = application,
                route = this,
                uri = path,
                parameters = parameters,
                replaceAll = true,
            )
        )
    }

    public fun replaceNamed(
        name: String,
        parameters: Parameters = Parameters.Empty,
        pathReplacements: Parameters = Parameters.Empty,
    ) {
        executeCall(
            VoyagerApplicationCall(
                application = application,
                route = this,
                name = name,
                parameters = parameters,
                pathReplacements = pathReplacements,
                replaceCurrent = true,
            )
        )
    }

    public fun replaceAllNamed(
        name: String,
        parameters: Parameters = Parameters.Empty,
        pathReplacements: Parameters = Parameters.Empty,
    ) {
        executeCall(
            VoyagerApplicationCall(
                application = application,
                route = this,
                name = name,
                parameters = parameters,
                pathReplacements = pathReplacements,
                replaceAll = true,
            )
        )
    }

    internal fun dispose() {
        application.dispose()
    }

    internal fun clearEvent() {
        navigation.tryEmit(value = VoyagerRouteEvent.Idle)
    }

    internal fun initialScreen(uri: String?): Screen {
        val path = when {
            uri.isNullOrBlank() -> application.environment.starterPath
            else -> uri
        }
        replace(path)
        // TODO: Returning empty screen for fast feedback while replace async
        // TODO: Maybe resolve directly and return a destination here??
        return EmptyScreen()
    }

    /**
     * Registers a function used to trace route resolution.
     * Might be useful if you need to understand why a route isn't executed.
     * To learn more, see [Tracing routes](https://ktor.io/docs/tracing-routes.html).
     */
    internal fun trace(block: (RoutingResolveTrace) -> Unit) {
        tracers.add(block)
    }

    internal fun registerNamed(name: String, route: VoyagerRoute) {
        check(!namedRoutes.containsKey(name)) {
            "Duplicated named route. Found '$name' to ${namedRoutes[name]} and $route"
        }
        namedRoutes[name] = route
    }

    internal fun mapNameToPath(name: String, pathReplacements: Parameters): String {
        val namedRoute =
            namedRoutes[name] ?: throw RouteNotFoundException(message = "Named route not found with name: $name")
        val routeSelectors = namedRoute.allSelectors()
        val skipPathParameters = routeSelectors.run {
            isEmpty() || all { selector -> selector is PathSegmentConstantRouteSelector }
        }
        if (skipPathParameters) {
            return namedRoute.toString()
        }

        return tryReplacePathParameters(
            routeName = name,
            namedRoute = namedRoute,
            pathParameters = pathReplacements,
            routeSelectors = routeSelectors,
        )
    }

    private fun tryReplacePathParameters(
        routeName: String,
        namedRoute: VoyagerRoute,
        pathParameters: Parameters,
        routeSelectors: List<RouteSelector>,
    ): String {
        val destination = mutableListOf<String>()
        for (selector in routeSelectors) {
            when (selector) {
                // Check constant value in path as 'hello' in /hello
                is PathSegmentConstantRouteSelector -> {
                    destination += selector.value
                }

                // Check optional {value?} path parameter
                is PathSegmentOptionalParameterRouteSelector -> {
                    val param = pathParameters[selector.name]
                    if (!param.isNullOrBlank()) {
                        destination += param
                    }
                }

                // Check required {value} path parameter
                is PathSegmentParameterRouteSelector -> {
                    val param = pathParameters[selector.name]
                    assertParameters(
                        routeName = routeName,
                        namedRoute = namedRoute,
                        parameterName = selector.name,
                        predicate = param::isNullOrBlank,
                    )
                    destination += param!!
                }

                // Check tailcard {value...} or {...} path parameter
                is PathSegmentTailcardRouteSelector -> {
                    if (selector.name.isNotBlank()) {
                        val values = pathParameters.getAll(selector.name)
                        assertParameters(
                            routeName = routeName,
                            namedRoute = namedRoute,
                            parameterName = selector.name,
                            predicate = values::isNullOrEmpty,
                        )
                        destination.addAll(values!!)
                    } else {
                        assertParameters(
                            routeName = routeName,
                            namedRoute = namedRoute,
                            parameterName = selector.name,
                            predicate = pathParameters::isEmpty,
                        )
                        pathParameters.forEach { _, values ->
                            destination.addAll(values)
                        }
                    }
                }

                // Check * path parameter
                is PathSegmentWildcardRouteSelector -> {
                    val names = pathParameters.names()
                    assertParameters(
                        routeName = routeName,
                        namedRoute = namedRoute,
                        parameterName = selector.toString(),
                        predicate = names::isEmpty,
                    )
                    val values = pathParameters.getAll(names.first())
                    assertParameters(
                        routeName = routeName,
                        namedRoute = namedRoute,
                        parameterName = selector.toString(),
                        predicate = values::isNullOrEmpty,
                    )
                    destination.addAll(values!!)
                }
            }
        }
        return destination.joinToString(separator = "/")
    }

    private fun executeCall(call: ApplicationCall) {
        with(application) {
            launch {
                execute(call)
            }
        }
    }

    private fun addDefaultTracing() {
        if (!application.log.isTraceEnabled) return

        tracers.add {
            application.log.trace(it.buildText())
        }
    }

    @Throws(MissingRequestParameterException::class)
    private fun assertParameters(
        routeName: String,
        namedRoute: VoyagerRoute,
        parameterName: String,
        predicate: () -> Boolean,
    ) {
        if (predicate()) {
            throw MissingRequestParameterException(
                parameterName = parameterName,
                message = "Parameter $parameterName is missing to route named '$routeName' and path: $namedRoute"
            )
        }
    }

    private suspend fun interceptor(context: PipelineContext<Unit, ApplicationCall>) {
        var call = requireNotNull(context.call as? VoyagerApplicationCall) {
            "Not supported call type: ${context.call}"
        }

        if (call.isPop) {
            val event = when (val predicate = call.popUntilPredicate) {
                null -> VoyagerRouteEvent.Pop
                else -> VoyagerRouteEvent.PopUntil(predicate)
            }
            navigation.emit(value = event)
            return
        }

        if (call.name.isNotBlank()) {
            call = call.copy(
                name = "",
                uri = mapNameToPath(
                    name = call.name,
                    pathReplacements = call.pathReplacements,
                )
            )
        }

        resolveCall(call)
    }

    private suspend fun resolveCall(call: VoyagerApplicationCall) {
        if (call.redirectAttempt > application.environment.maxRedirectAttempts) {
            throw TooManyRedirectException("Too many redirection. Last path try: ${call.uri}")
        }

        val resolveContext = RoutingResolveContext(this, call, tracers)
        when (val resolveResult = resolveContext.resolve()) {
            is RoutingResolveResult.Failure -> throw RouteNotFoundException(message = resolveResult.reason)

            is RoutingResolveResult.Success -> {
                val routingCallPipeline = resolveResult.route.buildPipeline()
                val routingCall = call.copy(parameters = call.parameters + resolveResult.parameters)
                routingCallPipeline.execute(routingCall)
            }
        }
    }

    /**
     * An installation object of the [VoyagerRouting] plugin.
     */
    @Suppress("PublicApiImplicitType")
    public companion object Plugin : BaseApplicationPlugin<Application, VoyagerRouting, VoyagerRouting> {

        override val key: AttributeKey<VoyagerRouting> = AttributeKey("Routing")

        override fun install(pipeline: Application, configure: VoyagerRouting.() -> Unit): VoyagerRouting {
            val routing = VoyagerRouting(pipeline).apply(configure)
            pipeline.intercept(Call) {
                routing.interceptor(this)
            }
            return routing
        }
    }
}

/**
 * Gets an [Application] for this [VoyagerRoute] by scanning the hierarchy to the root.
 */
public val VoyagerRoute.application: Application
    get() = when (this) {
        is VoyagerRouting -> application
        else -> parent?.application ?: throw UnsupportedOperationException(
            "Cannot retrieve application from unattached routing entry"
        )
    }

@KtorDsl
public fun voyagerRouting(
    parentCoroutineContext: CoroutineContext = EmptyCoroutineContext,
    log: Logger = KtorSimpleLogger("VoyagerRouting"),
    startDestination: String = "/",
    developmentMode: Boolean = false,
    maxRedirectAttempts: Int = 5,
    configuration: VoyagerRouting.() -> Unit
): VoyagerRouting {
    val environment = ApplicationEnvironment(
        parentCoroutineContext = parentCoroutineContext,
        log = log,
        starterPath = startDestination,
        developmentMode = developmentMode,
        maxRedirectAttempts = maxRedirectAttempts,
    )
    return with(Application(environment)) {
        install(VoyagerRouting, configuration)
    }
}
