package cafe.adriel.voyager.routing.core.redirect

import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.routing.core.application.ApplicationCall
import cafe.adriel.voyager.routing.core.application.call
import cafe.adriel.voyager.routing.core.routing.PipelineScreenInterceptor
import io.ktor.util.pipeline.PipelineContext

internal data class RedirectPipelineInterceptor(
    val name: String = "",
    val path: String = "",
) : PipelineScreenInterceptor<Unit, ApplicationCall> {

    init {
        validate()
    }

    override suspend fun invoke(context: PipelineContext<Unit, ApplicationCall>, p2: Unit): Screen {
        validate()
        return RedirectionScreen(name = name, path = path, call = context.call)
    }

    private fun validate() {
        check(name.isBlank() || path.isBlank()) {
            "To redirect you need provide a name or path. Found name: $name and path: $path"
        }
    }
}
