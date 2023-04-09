/*
* Copyright 2014-2021 JetBrains s.r.o and contributors. Use of this source code is governed by the Apache 2.0 license.
*/

package cafe.adriel.voyager.routing.core.routing

import cafe.adriel.voyager.routing.core.application.ApplicationCall
import cafe.adriel.voyager.routing.core.redirect.RedirectPipelineInterceptor
import io.ktor.util.KtorDsl

/**
 * Builds a route to match the specified [path].
 */
@KtorDsl
public fun VoyagerRoute.route(
    path: String,
    name: String? = null,
    build: VoyagerRoute.() -> Unit
): VoyagerRoute = createRouteFromPath(path, name).apply(build)

@KtorDsl
public fun VoyagerRoute.screen(
    path: String,
    name: String? = null,
    body: PipelineScreenInterceptor<Unit, ApplicationCall>,
): VoyagerRoute = route(path, name) { handle(body) }

@KtorDsl
public fun VoyagerRoute.screen(
    body: PipelineScreenInterceptor<Unit, ApplicationCall>,
) {
    check(this !is VoyagerRouting) {
        "Screen must be a child of Route. Found parent of: $this"
    }
    check(handler == null) {
        "Route multiple screen registration is not allowed"
    }
    handle(body)
}

@KtorDsl
public fun VoyagerRoute.redirectTo(
    name: String = "",
    path: String = "",
) {
    check(this !is VoyagerRouting) {
        "Redirect root is not allowed. You can do this changing rootPath on initialization"
    }
    val body = RedirectPipelineInterceptor(name = name, path = path)
    handle(body::invoke)
}

/**
 * Creates a routing entry for the specified path.
 */
internal fun VoyagerRoute.createRouteFromPath(path: String, name: String?): VoyagerRoute {
    val partAndSelector = mutableMapOf<String, RouteSelector>()
    val routingPath = RoutingPath.parse(path)
    var current: VoyagerRoute = this
    for (index in routingPath.parts.indices) {
        val (value, kind) = routingPath.parts[index]
        val selector = when (kind) {
            RoutingPathSegmentKind.Parameter -> PathSegmentSelectorBuilder.parseParameter(value)
            RoutingPathSegmentKind.Constant -> PathSegmentSelectorBuilder.parseConstant(value)
        }
        partAndSelector += value to selector
        // there may already be entry with same selector, so join them
        current = current.createChild(selector)
    }
    if (path.endsWith("/")) {
        current = current.createChild(TrailingSlashRouteSelector)
    }
    // Registering named route
    if (!name.isNullOrBlank()) {
        routing().registerNamed(
            name = name,
            named = VoyagerNamedRoute(
                routingPath = routingPath,
                partAndSelector = partAndSelector,
            )
        )
    }
    return current
}

/**
 * A helper object for building instances of [RouteSelector] from path segments.
 */
internal object PathSegmentSelectorBuilder {
    /**
     * Builds a [RouteSelector] to match a path segment parameter with a prefix/suffix and name.
     */
    public fun parseParameter(value: String): RouteSelector {
        val prefixIndex = value.indexOf('{')
        val suffixIndex = value.lastIndexOf('}')

        val prefix = if (prefixIndex == 0) null else value.substring(0, prefixIndex)
        val suffix = if (suffixIndex == value.length - 1) null else value.substring(suffixIndex + 1)

        val signature = value.substring(prefixIndex + 1, suffixIndex)
        return when {
            signature.endsWith("?") -> PathSegmentOptionalParameterRouteSelector(signature.dropLast(1), prefix, suffix)
            signature.endsWith("...") -> {
                if (!suffix.isNullOrEmpty()) {
                    throw IllegalArgumentException("Suffix after tailcard is not supported")
                }
                PathSegmentTailcardRouteSelector(signature.dropLast(3), prefix ?: "")
            }

            else -> PathSegmentParameterRouteSelector(signature, prefix, suffix)
        }
    }

    /**
     * Builds a [RouteSelector] to match a constant or wildcard segment parameter.
     */
    public fun parseConstant(value: String): RouteSelector = when (value) {
        "*" -> PathSegmentWildcardRouteSelector
        else -> PathSegmentConstantRouteSelector(value)
    }

    /**
     * Parses a name out of segment specification.
     */
    public fun parseName(value: String): String {
        val prefix = value.substringBefore('{', "")
        val suffix = value.substringAfterLast('}', "")
        val signature = value.substring(prefix.length + 1, value.length - suffix.length - 1)
        return when {
            signature.endsWith("?") -> signature.dropLast(1)
            signature.endsWith("...") -> signature.dropLast(3)
            else -> signature
        }
    }
}
