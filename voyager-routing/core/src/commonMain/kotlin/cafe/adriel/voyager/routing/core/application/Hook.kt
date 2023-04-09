package cafe.adriel.voyager.routing.core.application

import io.ktor.util.KtorDsl

/**
 * A hook that can be registered in [PluginBuilder].
 */
@KtorDsl
public interface Hook<HookHandler> {
    /**
     * Specifies how to install a hook in the [pipeline].
     */
    public fun install(pipeline: ApplicationCallPipeline, handler: HookHandler)
}

internal class HookHandler<T>(private val hook: Hook<T>, private val handler: T) {
    fun install(pipeline: ApplicationCallPipeline) {
        hook.install(pipeline, handler)
    }
}
