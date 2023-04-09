/*
* Copyright 2014-2021 JetBrains s.r.o and contributors. Use of this source code is governed by the Apache 2.0 license.
*/

package cafe.adriel.voyager.routing.core.application

import cafe.adriel.voyager.routing.core.routing.VoyagerApplicationCall
import cafe.adriel.voyager.routing.core.routing.VoyagerRoute
import cafe.adriel.voyager.routing.core.routing.VoyagerRouting
import cafe.adriel.voyager.routing.core.routing.application
import io.ktor.util.AttributeKey
import io.ktor.util.Attributes
import io.ktor.util.internal.initCauseBridge
import io.ktor.util.pipeline.Pipeline
import kotlinx.coroutines.CopyableThrowable
import kotlinx.coroutines.ExperimentalCoroutinesApi

/**
 * Defines an installable [Plugin](https://ktor.io/docs/plugins.html).
 * @param TPipeline is the type of the pipeline this plugin is compatible with
 * @param TConfiguration is the configuration object type for this Plugin
 * @param TPlugin is the instance type of the Plugin object
 */
@Suppress("AddVarianceModifier")
public interface Plugin<
    in TPipeline : Pipeline<*, ApplicationCall>,
    out TConfiguration : Any,
    TPlugin : Any> {
    /**
     * A unique key that identifies a plugin.
     */
    public val key: AttributeKey<TPlugin>

    /**
     * A plugin's installation script.
     */
    public fun install(pipeline: TPipeline, configure: TConfiguration.() -> Unit): TPlugin
}

/**
 * Defines a [Plugin](https://ktor.io/docs/plugins.html) that is installed into Application.
 * @param TPipeline is the type of the pipeline this plugin is compatible with
 * @param TConfiguration is the configuration object type for this Plugin
 * @param TPlugin is the instance type of the Plugin object
 */
public interface BaseApplicationPlugin<
    in TPipeline : Pipeline<*, ApplicationCall>,
    out TConfiguration : Any,
    TPlugin : Any> : Plugin<TPipeline, TConfiguration, TPlugin>

/**
 * Defines a [Plugin](https://ktor.io/docs/plugins.html) that is installed into Application.
 * @param TConfiguration is the configuration object type for this Plugin
 */
public interface ApplicationPlugin<out TConfiguration : Any> :
    BaseApplicationPlugin<Application, TConfiguration, PluginInstance>

internal val pluginRegistryKey = AttributeKey<Attributes>("ApplicationPluginRegistry")

/**
 * Returns the existing plugin registry or registers and returns a new one.
 */
public val <A : Pipeline<*, ApplicationCall>> A.pluginRegistry: Attributes
    get() = attributes.computeIfAbsent(pluginRegistryKey) { Attributes(true) }

/**
 * Gets a plugin instance for this pipeline, or fails with [MissingApplicationPluginException]
 * if the plugin is not installed.
 * @throws MissingApplicationPluginException
 * @param plugin [Plugin] to lookup
 * @return an instance of a plugin
 */
public fun <A : Pipeline<*, ApplicationCall>, F : Any> A.plugin(plugin: Plugin<*, *, F>): F {
    return when (this) {
        is VoyagerRoute -> findPluginInRoute(plugin)
        else -> pluginOrNull(plugin)
    } ?: throw MissingApplicationPluginException(plugin.key)
}

/**
 * Returns a plugin instance for this pipeline, or null if the plugin is not installed.
 */
public fun <A : Pipeline<*, ApplicationCall>, F : Any> A.pluginOrNull(plugin: Plugin<*, *, F>): F? {
    return pluginRegistry.getOrNull(plugin.key)
}

/**
 * Installs a [plugin] into this pipeline, if it is not yet installed.
 */
public fun <P : Pipeline<*, ApplicationCall>, B : Any, F : Any> P.install(
    plugin: Plugin<P, B, F>,
    configure: B.() -> Unit = {}
): F {
    if (this is VoyagerRoute && plugin is BaseRouteScopedPlugin) {
        return installIntoRoute(plugin, configure)
    }

    val registry = pluginRegistry
    return when (val installedPlugin = registry.getOrNull(plugin.key)) {
        null -> {
            try {
                val installed = plugin.install(this, configure)
                registry.put(plugin.key, installed)
                // environment.log.trace("`${plugin.name}` plugin was installed successfully.")
                installed
            } catch (t: Throwable) {
                // environment.log.error("`${plugin.name}` plugin failed to install.", t)
                throw t
            }
        }

        plugin -> {
            // environment.log.warning("`${plugin.name}` plugin is already installed")
            installedPlugin
        }

        else -> {
            throw DuplicatePluginException(
                "Please make sure that you use unique name for the plugin and don't install it twice. " +
                    "Conflicting application plugin is already installed with the same key as `${plugin.key.name}`"
            )
        }
    }
}

private fun <B : Any, F : Any> VoyagerRoute.installIntoRoute(
    plugin: BaseRouteScopedPlugin<B, F>,
    configure: B.() -> Unit = {}
): F {
    if (pluginRegistry.getOrNull(plugin.key) != null) {
        throw DuplicatePluginException(
            "Please make sure that you use unique name for the plugin and don't install it twice. " +
                "Plugin `${plugin.key.name}` is already installed to the pipeline $this"
        )
    }
    if (application.pluginRegistry.getOrNull(plugin.key) != null) {
        throw DuplicatePluginException(
            "Installing RouteScopedPlugin to application and route is not supported. " +
                "Consider moving application level install to routing root."
        )
    }
    // we install plugin into fake pipeline and add interceptors manually
    // to avoid having multiple interceptors after pipelines are merged
    val fakePipeline = when (this) {
        is VoyagerRouting -> VoyagerRouting(application)
        else -> VoyagerRoute(parent, selector, developmentMode, environment)
    }
    val installed = plugin.install(fakePipeline, configure)
    pluginRegistry.put(plugin.key, installed)

    mergePhases(fakePipeline)

    addAllInterceptors(fakePipeline, plugin, installed)

    return installed
}

private fun <B : Any, F : Any, TSubject, TContext, P : Pipeline<TSubject, TContext>> P.addAllInterceptors(
    fakePipeline: P,
    plugin: BaseRouteScopedPlugin<B, F>,
    pluginInstance: F
) {
    items.forEach { phase ->
        fakePipeline.interceptorsForPhase(phase)
            .forEach { interceptor ->
                intercept(phase) { subject ->
                    val call = context
                    if (call is VoyagerApplicationCall && call.routing.findPluginInRoute(plugin) == pluginInstance) {
                        interceptor(this, subject)
                    }
                }
            }
    }
}

/**
 * Thrown on an attempt to install the plugin with the same key as for the already installed plugin.
 */
public class DuplicatePluginException(message: String) : Exception(message)

/**
 * Thrown on an attempt to access the plugin that is not yet installed.
 * @param key application plugin's attribute key
 */
@OptIn(ExperimentalCoroutinesApi::class)
public class MissingApplicationPluginException(
    public val key: AttributeKey<*>
) : IllegalStateException(), CopyableThrowable<MissingApplicationPluginException> {
    override val message: String get() = "Application plugin ${key.name} is not installed"

    override fun createCopy(): MissingApplicationPluginException = MissingApplicationPluginException(key).also {
        it.initCauseBridge(this)
    }
}
