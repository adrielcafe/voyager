package cafe.adriel.voyager.core.hook

import androidx.compose.runtime.ProvidedValue
import cafe.adriel.voyager.core.screen.Screen

public typealias HookableScreen = Hookable<ScreenHook>

public val Screen.hooks: ScreenHooks
    get() = when (this) {
        is Hookable<*> -> ScreenHooks(
            providers = hooks.filterIsInstance<ScreenHook.OnProvide<*>>(),
            disposers = hooks.filterIsInstance<ScreenHook.OnDispose>(),
        )
        else -> ScreenHooks()
    }

public fun Screen.clearHooks() {
    if (this is Hookable<*>) {
        clearHooks()
    }
}

public sealed class ScreenHook {
    public data class OnProvide<T>(val provide: () -> ProvidedValue<T>) : ScreenHook()
    public data class OnDispose(val dispose: () -> Unit) : ScreenHook()
}

public data class ScreenHooks(
    val providers: List<ScreenHook.OnProvide<*>> = emptyList(),
    val disposers: List<ScreenHook.OnDispose> = emptyList()
)

public class ScreenHookHandler : HookableScreen {

    private val mutableHooks = mutableListOf<ScreenHook>()

    override val hooks: List<ScreenHook>
        get() = mutableHooks

    override fun addHooks(hooks: List<ScreenHook>) {
        mutableHooks += hooks
    }

    override fun removeHooks(hooks: List<ScreenHook>) {
        mutableHooks -= hooks
    }

    override fun clearHooks() {
        mutableHooks.clear()
    }
}
