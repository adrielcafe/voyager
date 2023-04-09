/*
 * Copyright 2014-2022 JetBrains s.r.o and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package cafe.adriel.voyager.routing.core.application.hooks

import cafe.adriel.voyager.routing.core.application.ApplicationCall
import cafe.adriel.voyager.routing.core.application.ApplicationCallPipeline
import cafe.adriel.voyager.routing.core.application.Hook
import cafe.adriel.voyager.routing.core.application.call
import io.ktor.events.EventDefinition
import io.ktor.util.InternalAPI
import io.ktor.util.pipeline.PipelinePhase
import kotlinx.coroutines.coroutineScope

/**
 * A hook that is invoked as a first step in processing a call.
 * Useful for validating, updating a call based on proxy information, etc.
 */
public object CallSetup : Hook<suspend (ApplicationCall) -> Unit> {
    override fun install(pipeline: ApplicationCallPipeline, handler: suspend (ApplicationCall) -> Unit) {
        pipeline.intercept(ApplicationCallPipeline.Setup) {
            handler(call)
        }
    }
}

/**
 * A hook that is invoked when a call fails with an exception.
 */
public object CallFailed : Hook<suspend (call: ApplicationCall, cause: Throwable) -> Unit> {

    private val phase = PipelinePhase("BeforeSetup")
    override fun install(
        pipeline: ApplicationCallPipeline,
        handler: suspend (call: ApplicationCall, cause: Throwable) -> Unit
    ) {
        pipeline.insertPhaseBefore(ApplicationCallPipeline.Setup, phase)
        pipeline.intercept(phase) {
            try {
                coroutineScope {
                    proceed()
                }
            } catch (cause: Throwable) {
                handler(call, cause)
            }
        }
    }
}

/**
 * A shortcut hook for [cafe.adriel.voyager.routing.core.application.ApplicationEnvironment.monitor] subscription.
 */
public class MonitoringEvent<Param : Any, Event : EventDefinition<Param>>(
    private val event: Event
) : Hook<(Param) -> Unit> {
    override fun install(pipeline: ApplicationCallPipeline, handler: (Param) -> Unit) {
        pipeline.environment!!.monitor.subscribe(event) {
            handler(it)
        }
    }
}

/**
 * A hook that is invoked before routing and most of the plugins.
 * Useful for metrics, logging, etc.
 *
 * Can be renamed or removed from public API in the future.
 */
@InternalAPI
public object Metrics : Hook<suspend (ApplicationCall) -> Unit> {
    override fun install(pipeline: ApplicationCallPipeline, handler: suspend (ApplicationCall) -> Unit) {
        pipeline.intercept(ApplicationCallPipeline.Monitoring) {
            handler(call)
        }
    }
}
