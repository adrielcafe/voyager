/*
 * Copyright 2014-2021 JetBrains s.r.o and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package cafe.adriel.voyager.routing.core.application

import io.ktor.util.KtorDsl
import io.ktor.util.pipeline.PipelineContext

/**
 * The context associated with the call that is currently being processed by server.
 * Every call handler ([PluginBuilder.onCall], [PluginBuilder.onCallReceive], [PluginBuilder.onCallRespond], and so on)
 * of your plugin has a derivative of [CallContext] as a receiver.
 **/
@KtorDsl
public open class CallContext<PluginConfig : Any> internal constructor(
    public val pluginConfig: PluginConfig,
    protected open val context: PipelineContext<*, ApplicationCall>
) {
    // Internal usage for tests only
    internal fun finish() = context.finish()
}

/**
 * A context associated with the call handling by your application. [OnCallContext] is a receiver for [PluginBuilder.onCall] handler
 * of your [PluginBuilder].
 *
 * @see CallContext
 **/
@KtorDsl
public class OnCallContext<PluginConfig : Any> internal constructor(
    pluginConfig: PluginConfig,
    context: PipelineContext<Unit, ApplicationCall>
) : CallContext<PluginConfig>(pluginConfig, context)
