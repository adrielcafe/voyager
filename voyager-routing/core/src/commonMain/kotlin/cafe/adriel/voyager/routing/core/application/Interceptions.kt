/*
 * Copyright 2014-2021 JetBrains s.r.o and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package cafe.adriel.voyager.routing.core.application

import io.ktor.util.pipeline.Pipeline
import io.ktor.util.pipeline.PipelinePhase

/**
 * A thin wrapper over pipelines and phases API. It usually wraps `pipeline.intercept(phase) { ... }`
 * statement.
 **/
internal class Interception<T : Any>(
    val phase: PipelinePhase,
    val action: (Pipeline<T, ApplicationCall>) -> Unit
)

/**
 * An instance of [Interception] for the call phase, i.e. an [Interception] that takes place inside onCall { ... } handler.
 **/
internal typealias CallInterception = Interception<Unit>
