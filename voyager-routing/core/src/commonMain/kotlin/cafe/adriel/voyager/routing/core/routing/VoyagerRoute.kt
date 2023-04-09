/*
 * Copyright 2014-2019 JetBrains s.r.o and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package cafe.adriel.voyager.routing.core.routing

import cafe.adriel.voyager.routing.core.application.ApplicationCall
import cafe.adriel.voyager.routing.core.application.ApplicationCallPipeline
import cafe.adriel.voyager.routing.core.application.ApplicationEnvironment
import cafe.adriel.voyager.routing.core.application.call
import cafe.adriel.voyager.routing.core.redirect.RedirectionScreen
import io.ktor.util.KtorDsl
import io.ktor.util.pipeline.execute

/**
 * Describes a node in a routing tree.
 *
 * @param parent is a parent node in the tree, or null for root node.
 * @param selector is an instance of [RouteSelector] for this node.
 */
@KtorDsl
public open class VoyagerRoute(
    public val parent: VoyagerRoute?,
    public val selector: RouteSelector,
    developmentMode: Boolean = false,
    environment: ApplicationEnvironment? = null
) : ApplicationCallPipeline(developmentMode, environment) {

    /**
     * List of child routes for this node.
     */
    public val children: List<VoyagerRoute> get() = childList

    private val childList: MutableList<VoyagerRoute> = mutableListOf()

    private var cachedPipeline: ApplicationCallPipeline? = null

    internal var handler: PipelineScreenInterceptor<Unit, ApplicationCall>? = null

    /**
     * Creates a child node in this node with a given [selector] or returns an existing one with the same selector.
     */
    public fun createChild(selector: RouteSelector): VoyagerRoute {
        val existingEntry = childList.firstOrNull { it.selector == selector }
        if (existingEntry == null) {
            val entry = VoyagerRoute(this, selector, developmentMode, environment)
            childList.add(entry)
            return entry
        }
        return existingEntry
    }

    /**
     * Allows using a route instance for building additional routes.
     */
    public operator fun invoke(body: VoyagerRoute.() -> Unit): Unit = body()

    /**
     * Installs a handler into this route which is called when the route is selected for a call.
     */
    public fun handle(handler: PipelineScreenInterceptor<Unit, ApplicationCall>) {
        this.handler = handler

        // Adding a handler invalidates only pipeline for this entry
        cachedPipeline = null
    }

    override fun afterIntercepted() {
        // Adding an interceptor invalidates pipelines for all children
        // We don't need synchronisation here, because order of intercepting and acquiring pipeline is indeterminate
        // If some child already cached its pipeline, it's ok to execute with outdated pipeline
        invalidateCachesRecursively()
    }

    private fun invalidateCachesRecursively() {
        cachedPipeline = null
        childList.forEach { it.invalidateCachesRecursively() }
    }

    override fun toString(): String {
        return when (val parentRoute = parent?.toString()) {
            null -> when (selector) {
                is TrailingSlashRouteSelector -> "/"
                else -> "/$selector"
            }

            else -> when (selector) {
                is TrailingSlashRouteSelector -> if (parentRoute.endsWith('/')) parentRoute else "$parentRoute/"
                else -> if (parentRoute.endsWith('/')) "$parentRoute$selector" else "$parentRoute/$selector"
            }
        }
    }

    internal fun buildPipeline(): ApplicationCallPipeline = cachedPipeline ?: run {
        var current: VoyagerRoute? = this
        val pipeline = ApplicationCallPipeline(developmentMode, application.environment)
        val routePipelines = mutableListOf<ApplicationCallPipeline>()
        while (current != null) {
            routePipelines.add(current)
            current = current.parent
        }

        for (index in routePipelines.lastIndex downTo 0) {
            val routePipeline = routePipelines[index]
            pipeline.merge(routePipeline)
        }

        val handler = requireNotNull(handler) {
            "Handler not found for the route: $this"
        }

        pipeline.intercept(Call) {
            val routingCall = requireNotNull(call as? VoyagerApplicationCall) {
                "Call not supported: $call"
            }
            val screen = handler.invoke(this, Unit)

            if (screen is RedirectionScreen) {
                val uri = when {
                    screen.name.isBlank() -> screen.path
                    else -> routingCall.routing.mapNameToPath(
                        name = screen.name,
                        pathReplacements = routingCall.pathReplacements,
                    )
                }
                routingCall.routing.application.execute(
                    routingCall.copy(
                        uri = uri,
                        redirectAttempt = routingCall.redirectAttempt + 1,
                    )
                )
                return@intercept
            }

            val event = when {
                routingCall.replaceAll || routingCall.replaceCurrent ->
                    VoyagerRouteEvent.Replace(screen, routingCall.replaceAll)

                else -> VoyagerRouteEvent.Push(screen)
            }

            routingCall.routing.navigation.emit(event)
        }

        cachedPipeline = pipeline
        pipeline
    }
}

internal fun VoyagerRoute.allSelectors(): List<RouteSelector> {
    val selectors = mutableListOf(selector)
    var other = parent
    while (other != null && other !is VoyagerRouting) {
        selectors += other.selector
        other = other.parent
    }
    // We need reverse to starting from top-most parent
    return selectors.reversed()
}

/**
 * Return list of endpoints with handlers under this route.
 */
public fun VoyagerRoute.getAllRoutes(): List<VoyagerRoute> {
    val endpoints = mutableListOf<VoyagerRoute>()
    getAllRoutes(endpoints)
    return endpoints
}

private fun VoyagerRoute.getAllRoutes(endpoints: MutableList<VoyagerRoute>) {
    if (handler != null) {
        endpoints.add(this)
    }
    children.forEach { it.getAllRoutes(endpoints) }
}

public fun VoyagerRoute.routing(): VoyagerRouting {
    var parent: VoyagerRoute? = this
    while (parent != null) {
        if (parent is VoyagerRouting) {
            return parent
        }
        parent = parent.parent
    }
    error("Route not attached to a parent VoyagerRouting: $this")
}
