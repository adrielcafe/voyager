package cafe.adriel.voyager.core.lifecycle

import cafe.adriel.voyager.core.concurrent.ThreadSafeMap
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import kotlin.reflect.KType
import kotlin.reflect.typeOf

public object ScreenLifecycleStore {

    private val owners = ThreadSafeMap<ScreenKey, ScreenLifecycleOwner>()
    private val newOwners = ThreadSafeMap<ScreenKey, ThreadSafeMap<KType, ScreenDisposable>>()

    @Deprecated(
        message = "Use `register` instead. Will be removed in 1.0.0.",
        replaceWith = ReplaceWith("ScreenLifecycleStore.register<T>(screen, factory)"),
    )
    public fun get(
        screen: Screen,
        factory: (ScreenKey) -> ScreenLifecycleOwner
    ): ScreenLifecycleOwner =
        owners.getOrPut(screen.key) { factory(screen.key) }

    /**
     * Register a ScreenDisposable that will be called `onDispose` on the
     * [screen] leaves the Navigation stack.
     */
    public inline fun <reified T : ScreenDisposable> register(
        screen: Screen,
        noinline factory: (ScreenKey) -> T,
    ): T {
        return register(screen, typeOf<T>(), factory) as T
    }

    @PublishedApi
    internal fun <T : ScreenDisposable> register(
        screen: Screen,
        screenDisposeListenerType: KType,
        factory: (ScreenKey) -> T,
    ): ScreenDisposable {
        return newOwners.getOrPut(screen.key) {
            ThreadSafeMap<KType, ScreenDisposable>().apply {
                put(screenDisposeListenerType, factory(screen.key))
            }
        }.getOrPut(screenDisposeListenerType) {
            factory(screen.key)
        }
    }

    public fun remove(screen: Screen) {
        owners.remove(screen.key)?.onDispose(screen)
        newOwners.remove(screen.key)?.forEach { it.value.onDispose(screen) }
    }
}
