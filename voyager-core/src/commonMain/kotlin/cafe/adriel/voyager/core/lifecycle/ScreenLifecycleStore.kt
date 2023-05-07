package cafe.adriel.voyager.core.lifecycle

import cafe.adriel.voyager.core.concurrent.ThreadSafeMap
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import kotlin.reflect.KType
import kotlin.reflect.typeOf

public object ScreenLifecycleStore {

    private val owners = ThreadSafeMap<ScreenKey, ScreenLifecycleOwner>()
    private val newOwners = ThreadSafeMap<ScreenKey, ThreadSafeMap<KType, ScreenLifecycleOwner>>()

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
     * Register a ScreenLifecycleOwner that will be called `onDispose` on the
     * [screen] leaves the Navigation stack.
     */
    public inline fun <reified T : ScreenLifecycleOwner> register(
        screen: Screen,
        noinline factory: (ScreenKey) -> T,
    ): T {
        return register(screen, typeOf<T>(), factory) as T
    }

    @PublishedApi
    internal fun <T : ScreenLifecycleOwner> register(
        screen: Screen,
        screenLifecycleOwnerType: KType,
        factory: (ScreenKey) -> T,
    ): ScreenLifecycleOwner {
        return newOwners.getOrPut(screen.key) {
            ThreadSafeMap<KType, ScreenLifecycleOwner>().apply {
                put(screenLifecycleOwnerType, factory(screen.key))
            }
        }.getOrPut(screenLifecycleOwnerType) {
            factory(screen.key)
        }
    }

    public fun remove(screen: Screen) {
        owners.remove(screen.key)?.onDispose(screen)
        newOwners.remove(screen.key)?.forEach { it.value.onDispose(screen) }
    }
}
