/*
 * Copyright 2014-2021 JetBrains s.r.o and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package cafe.adriel.voyager.routing.core.application

import cafe.adriel.voyager.routing.core.routing.VoyagerRoute
import cafe.adriel.voyager.routing.core.routing.VoyagerRouting
import cafe.adriel.voyager.routing.core.routing.application

/**
 * Defines a [Plugin](https://ktor.io/docs/plugins.html) that can be installed into a [VoyagerRoute].
 * @param TConfiguration is the configuration object type for this Plugin
 * @param TPlugin is the instance type of the Plugin object
 */
public interface BaseRouteScopedPlugin<TConfiguration : Any, TPlugin : Any> :
    Plugin<ApplicationCallPipeline, TConfiguration, TPlugin>

/**
 * Defines a Plugin that can be installed into [VoyagerRoute]
 * @param TConfiguration is the configuration object type for this Plugin
 */
public interface RouteScopedPlugin<TConfiguration : Any> : BaseRouteScopedPlugin<TConfiguration, PluginInstance>

/**
 * Finds the plugin [F] in the current [VoyagerRoute]. If not found, search in the parent [VoyagerRoute].
 *
 * @return [F] instance or `null` if not found
 */
public fun <F : Any> VoyagerRoute.findPluginInRoute(plugin: Plugin<*, *, F>): F? {
    var current = this
    while (true) {
        val installedFeature = current.pluginOrNull(plugin)
        if (installedFeature != null) {
            return installedFeature
        }
        if (current.parent == null) {
            break
        }
        current = current.parent!!
    }
    if (current is VoyagerRouting) {
        return application.pluginOrNull(plugin)
    }
    return null
}
