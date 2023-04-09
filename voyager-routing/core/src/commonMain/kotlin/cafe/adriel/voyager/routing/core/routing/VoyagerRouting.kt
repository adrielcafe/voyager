/*
* Copyright 2014-2021 JetBrains s.r.o and contributors. Use of this source code is governed by the Apache 2.0 license.
*/

package cafe.adriel.voyager.routing.core.routing

import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.routing.core.application.Application
import cafe.adriel.voyager.routing.core.application.ApplicationCall
import cafe.adriel.voyager.routing.core.application.ApplicationEnvironment
import cafe.adriel.voyager.routing.core.application.BaseApplicationPlugin
import cafe.adriel.voyager.routing.core.application.Plugin
import cafe.adriel.voyager.routing.core.application.call
import cafe.adriel.voyager.routing.core.application.install
import cafe.adriel.voyager.routing.core.application.pluginOrNull
import cafe.adriel.voyager.routing.core.plugins.RouteNotFoundException
import cafe.adriel.voyager.routing.core.plugins.TooManyRedirectException
import cafe.adriel.voyager.routing.core.screens.EmptyScreen
import io.ktor.events.EventDefinition
import io.ktor.http.Parameters
import io.ktor.http.ParametersBuilder
import io.ktor.http.appendUrlFullPath
import io.ktor.util.AttributeKey
import io.ktor.util.KtorDsl
import io.ktor.util.logging.KtorSimpleLogger
import io.ktor.util.logging.isTraceEnabled
import io.ktor.util.pipeline.PipelineContext
import io.ktor.util.pipeline.execute
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

internal val LOGGER = KtorSimpleLogger("io.ktor.routing.Routing")

/**
 * A root routing node.
 * You can learn more about routing in Ktor from [Routing](https://ktor.io/docs/routing-in-ktor.html).
 */
@KtorDsl
public class VoyagerRouting internal constructor(
    internal val application: Application,
) : VoyagerRoute(
    parent = null,
    selector = RootRouteSelector(application.environment.rootPath),
    application.environment.developmentMode,
    application.environment
) {
    private val tracers = mutableListOf<(RoutingResolveTrace) -> Unit>()
    private val namedPaths = mutableMapOf<String, VoyagerNamedRoute>()
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

    public fun push(path: String) {
        executeCall(VoyagerApplicationCall(application = application, route = this, uri = path))
    }

    public fun pushNamed(
        name: String,
        pathParameters: Parameters = Parameters.Empty,
        queryParameters: Parameters = Parameters.Empty,
    ) {
        executeCall(
            VoyagerApplicationCall(
                application = application,
                route = this,
                name = name,
                parameters = pathParameters,
                queryParameters = queryParameters,
            )
        )
    }

    public fun replace(path: String) {
        executeCall(
            VoyagerApplicationCall(
                application = application,
                route = this,
                uri = path,
                replaceCurrent = true,
            )
        )
    }

    public fun replaceAll(path: String) {
        executeCall(
            VoyagerApplicationCall(
                application = application,
                route = this,
                uri = path,
                replaceAll = true,
            )
        )
    }

    public fun replaceNamed(
        name: String,
        pathParameters: Parameters = Parameters.Empty,
        queryParameters: Parameters = Parameters.Empty,
    ) {
        executeCall(
            VoyagerApplicationCall(
                application = application,
                route = this,
                name = name,
                parameters = pathParameters,
                queryParameters = queryParameters,
                replaceCurrent = true,
            )
        )
    }

    public fun replaceAllNamed(
        name: String,
        pathParameters: Parameters = Parameters.Empty,
        queryParameters: Parameters = Parameters.Empty,
    ) {
        executeCall(
            VoyagerApplicationCall(
                application = application,
                route = this,
                name = name,
                parameters = pathParameters,
                queryParameters = queryParameters,
                replaceAll = true,
            )
        )
    }

    internal fun clearEvent() {
        navigation.tryEmit(value = VoyagerRouteEvent.Idle)
    }

    internal fun initialScreen(uri: String?): Screen {
        val path = when {
            uri.isNullOrBlank() -> application.environment.rootPath
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

    internal fun registerNamed(name: String, named: VoyagerNamedRoute) {
        check(!namedPaths.containsKey(name)) {
            "Duplicated route name. Found '$name' to ${namedPaths[name]?.routingPath} and ${named.routingPath}"
        }
        namedPaths[name] = named
    }

    internal fun executeCall(call: ApplicationCall) {
        with(application) {
            launch {
                execute(call)
            }
        }
    }

    private fun addDefaultTracing() {
        if (!LOGGER.isTraceEnabled) return

        tracers.add {
            LOGGER.trace(it.buildText())
        }
    }

    internal fun mapNameToPath(
        name: String,
        pathParameters: Parameters,
        queryParameters: Parameters,
    ): String {
        val routeNamed = namedPaths[name]
        checkNotNull(routeNamed) {
            "Named route not found with name: $name"
        }

        val skipParameters = routeNamed.partAndSelector.isEmpty() ||
            routeNamed.partAndSelector.all { it.value is PathSegmentConstantRouteSelector }
        if (skipParameters && queryParameters.isEmpty()) {
            return routeNamed.routingPath.toString()
        }

        val path = when {
            skipParameters -> routeNamed.routingPath.toString()
            else -> tryReplacePathParameters(routeNamed, pathParameters, name)
        }

        if (queryParameters.isEmpty()) {
            return path
        }

        val queryParametersBuilder = ParametersBuilder().apply {
            appendAll(queryParameters)
        }

        val builder = StringBuilder()
        builder.appendUrlFullPath(
            encodedPath = path,
            encodedQueryParameters = queryParametersBuilder,
            trailingQuery = false,
        )
        return builder.toString()
    }

    private fun tryReplacePathParameters(
        routeNamed: VoyagerNamedRoute,
        pathParameters: Parameters,
        name: String,
    ): String {
        val destination = mutableListOf<String>()
        for (index in routeNamed.routingPath.parts.indices) {
            val (value, _) = routeNamed.routingPath.parts[index]

            when (val selector = routeNamed.partAndSelector[value]) {
                // Check constant value in path as 'hello' in /hello
                is PathSegmentConstantRouteSelector -> {
                    destination += value
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
                    check(!param.isNullOrBlank()) {
                        "Path parameter $value ${provideErrorMessage(name, routeNamed.routingPath)}"
                    }
                    destination += param
                }

                // Check tailcard {value...} or {...} path parameter
                is PathSegmentTailcardRouteSelector -> {
                    if (selector.name.isNotBlank()) {
                        val values = pathParameters.getAll(selector.name)
                        check(!values.isNullOrEmpty()) {
                            "Tailcard parameter $value ${provideErrorMessage(name, routeNamed.routingPath)}"
                        }
                        destination.addAll(values)
                    } else {
                        check(!pathParameters.isEmpty()) {
                            "Tailcard parameters ${provideErrorMessage(name, routeNamed.routingPath)}"
                        }
                        pathParameters.forEach { _, values ->
                            destination.addAll(values)
                        }
                    }
                }

                // Check * path parameter
                is PathSegmentWildcardRouteSelector -> {
                    val names = pathParameters.names()
                    check(names.isNotEmpty()) {
                        "Wildcard parameters ${provideErrorMessage(name, routeNamed.routingPath)}"
                    }
                    check(names.size == 1) {
                        "Wildcard supports one parameter only. Route named '$name' received: $names"
                    }
                    val paramName = names.first()
                    val values = pathParameters.getAll(paramName)
                    if (!values.isNullOrEmpty()) {
                        check(values.size == 1) {
                            "Wildcard supports one value only. Route named '$name' received: $values"
                        }
                        destination.addAll(values)
                    }
                }
            }
        }
        return destination.joinToString(separator = "/")
    }

    private fun provideErrorMessage(routeName: String, routePath: RoutingPath): String =
        "not provided to named route '$routeName' with path: $routePath"

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
                    pathParameters = call.parameters,
                    queryParameters = call.queryParameters,
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

            is RoutingResolveResult.Success ->
                resolveSuccess(call, resolveResult.route, resolveResult.parameters)
        }
    }

    private suspend fun resolveSuccess(
        call: VoyagerApplicationCall,
        route: VoyagerRoute,
        parameters: Parameters
    ) {
        val routingCallPipeline = route.buildPipeline()
        val routingCall = call.copy(parameters = parameters)
        application.environment.monitor.raise(RoutingCallStarted, routingCall)
        try {
            routingCallPipeline.execute(routingCall)
        } finally {
            application.environment.monitor.raise(RoutingCallFinished, routingCall)
        }
    }

    /**
     * An installation object of the [VoyagerRouting] plugin.
     */
    @Suppress("PublicApiImplicitType")
    public companion object Plugin : BaseApplicationPlugin<Application, VoyagerRouting, VoyagerRouting> {

        /**
         * A definition for an event that is fired when routing-based call processing starts.
         */
        public val RoutingCallStarted: EventDefinition<ApplicationCall> = EventDefinition()

        /**
         * A definition for an event that is fired when routing-based call processing is finished.
         */
        public val RoutingCallFinished: EventDefinition<ApplicationCall> = EventDefinition()

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

/**
 * Installs a [plugin] into this pipeline, if it is not yet installed.
 */
// TODO: Change extension to VoyagerRoute to support route scoped plugins
public fun <B : Any, F : Any> VoyagerRouting.install(
    plugin: Plugin<Application, B, F>,
    configure: B.() -> Unit = {}
): F = application.install(plugin, configure)

@KtorDsl
public fun voyagerRouting(
    environment: ApplicationEnvironment,
    configuration: VoyagerRouting.() -> Unit
): VoyagerRouting {
    // TODO: Creating application instance here always install the plugin instead of reuse
    return with(Application(environment)) {
        pluginOrNull(VoyagerRouting)?.apply(configuration) ?: install(VoyagerRouting, configuration)
    }
}
