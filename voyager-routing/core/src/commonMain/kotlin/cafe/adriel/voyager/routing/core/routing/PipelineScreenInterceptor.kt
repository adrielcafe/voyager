package cafe.adriel.voyager.routing.core.routing

import cafe.adriel.voyager.core.screen.Screen
import io.ktor.util.pipeline.PipelineContext

public typealias PipelineScreenInterceptor<TSubject, TContext> =
    suspend PipelineContext<TSubject, TContext>.(TSubject) -> Screen
