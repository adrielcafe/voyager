/*
 * Copyright 2014-2021 JetBrains s.r.o and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package cafe.adriel.voyager.routing.core.application

import cafe.adriel.voyager.routing.core.application.debug.PHASE_ON_CALL
import cafe.adriel.voyager.routing.core.application.debug.ijDebugReportHandlerFinished
import cafe.adriel.voyager.routing.core.application.debug.ijDebugReportHandlerStarted
import io.ktor.util.AttributeKey
import io.ktor.util.KtorDsl
import io.ktor.util.debug.addToContextInDebugMode
import io.ktor.util.pipeline.PipelineContext
import io.ktor.util.pipeline.PipelinePhase
import kotlin.random.Random

/**
 * A utility class to build an [ApplicationPlugin] instance.
 **/
@KtorDsl
public abstract class PluginBuilder<PluginConfig : Any> internal constructor(
    internal val key: AttributeKey<PluginInstance>
) {

    /**
     * A reference to the [Application] where the plugin is installed.
     */
    public abstract val application: Application

    /**
     * A configuration of the current plugin.
     */
    public abstract val pluginConfig: PluginConfig

    /**
     * A pipeline [PluginConfig] for the current plugin.
     * See [Pipelines](https://ktor.io/docs/pipelines.html) for more information.
     **/
    internal abstract val pipeline: ApplicationCallPipeline

    /**
     * Allows you to access the environment of the currently running application where the plugin is installed.
     **/
    public val environment: ApplicationEnvironment? get() = pipeline.environment

    internal val callInterceptions: MutableList<CallInterception> = mutableListOf()

    internal val hooks: MutableList<HookHandler<*>> = mutableListOf()

    /**
     * Specifies the [block] handler for every incoming [ApplicationCall].
     *
     * This block is invoked for every incoming call even if the call is already handled by other handler.
     * There you can handle the call in a way you want: add headers, change the response status, etc. You can also
     * access the external state to calculate stats.
     *
     * This example demonstrates how to create a plugin that appends a custom header to each response:
     * ```kotlin
     * val CustomHeaderPlugin = createApplicationPlugin(name = "CustomHeaderPlugin") {
     *     onCall { call ->
     *         call.response.headers.append("X-Custom-Header", "Hello, world!")
     *     }
     * }
     * ```
     *
     * @see [createApplicationPlugin]
     *
     * @param block An action that needs to be executed when your application receives an HTTP call.
     **/
    public fun onCall(block: suspend OnCallContext<PluginConfig>.(call: ApplicationCall) -> Unit) {
        onDefaultPhase(
            callInterceptions,
            ApplicationCallPipeline.Plugins,
            PHASE_ON_CALL,
            ::OnCallContext
        ) { call, _ ->
            block(call)
        }
    }

    /**
     * Specifies a [handler] for a specific [hook].
     * A [hook] can be a specific place in time or event during the request
     * processing like application shutdown, an exception during call processing, etc.
     * @see [createApplicationPlugin]
     *
     * Example:
     * ```kotlin
     * val ResourceManager = createApplicationPlugin("ResourceManager") {
     *     val resources: List<Closeable> = TODO()
     *
     *     on(MonitoringEvent(ApplicationStopped)) {
     *         resources.forEach { it.close() }
     *     }
     * }
     * ```
     */
    public fun <HookHandler> on(
        hook: Hook<HookHandler>,
        handler: HookHandler
    ) {
        hooks.add(HookHandler(hook, handler))
    }

    private fun <T : Any, ContextT : CallContext<PluginConfig>> onDefaultPhaseWithMessage(
        interceptions: MutableList<Interception<T>>,
        phase: PipelinePhase,
        handlerName: String,
        contextInit: (pluginConfig: PluginConfig, PipelineContext<T, ApplicationCall>) -> ContextT,
        block: suspend ContextT.(ApplicationCall, T) -> Unit
    ) {
        interceptions.add(
            Interception(
                phase,
                action = { pipeline ->
                    pipeline.intercept(phase) {
                        // Information about the plugin name is needed for the Intellij Idea debugger.
                        val key = this@PluginBuilder.key
                        val pluginConfig = this@PluginBuilder.pluginConfig
                        addToContextInDebugMode(key.name) {
                            ijDebugReportHandlerStarted(pluginName = key.name, handler = handlerName)

                            // Perform current plugin's handler
                            contextInit(pluginConfig, this@intercept).block(call, subject)

                            ijDebugReportHandlerFinished(pluginName = key.name, handler = handlerName)
                        }
                    }
                }
            )
        )
    }

    private fun <T : Any, ContextT : CallContext<PluginConfig>> onDefaultPhase(
        interceptions: MutableList<Interception<T>>,
        phase: PipelinePhase,
        handlerName: String,
        contextInit: (pluginConfig: PluginConfig, PipelineContext<T, ApplicationCall>) -> ContextT,
        block: suspend ContextT.(call: ApplicationCall, body: T) -> Unit
    ) {
        onDefaultPhaseWithMessage(interceptions, phase, handlerName, contextInit) { call, body -> block(call, body) }
    }

    internal fun newPhase(): PipelinePhase = PipelinePhase("${key.name}Phase${Random.nextInt()}")
}
