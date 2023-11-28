package cafe.adriel.voyager.core.lifecycle

import cafe.adriel.voyager.core.concurrent.ThreadSafeMap
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import kotlin.reflect.KType
import kotlin.reflect.typeOf

public object ScreenLifecycleStore {

    private val owners = ThreadSafeMap<ScreenKey, ScreenLifecycleOwner>()
    private val newOwners = ThreadSafeMap<ScreenKey, ThreadSafeMap<KType, ScreenDisposable>>()

    /**
     * Get current for screen or register a ScreenDisposable resulted by
     * [factory] that will be called `onDispose` on the [screen] leaves
     * the Navigation stack.
     */
    public inline fun <reified T : ScreenDisposable> get(
        screen: Screen,
        noinline factory: (ScreenKey) -> T
    ): T {
        return get(screen, typeOf<T>(), factory) as T
    }

    /**
     * Get current for screen or register a ScreenDisposable resulted by
     * [factory] that will be called `onDispose` on the [screen] leaves
     * the Navigation stack.
     */
    @Deprecated(
        message = "Use `get` instead. Will be removed in 1.1.0.",
        replaceWith = ReplaceWith("ScreenLifecycleStore.get<T>(screen, factory)"),
        level = DeprecationLevel.HIDDEN
    )
    public inline fun <reified T : ScreenDisposable> register(
        screen: Screen,
        noinline factory: (ScreenKey) -> T
    ): T {
        return get(screen, factory)
    }

    @PublishedApi
    internal fun <T : ScreenDisposable> get(
        screen: Screen,
        screenDisposeListenerType: KType,
        factory: (ScreenKey) -> T
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
