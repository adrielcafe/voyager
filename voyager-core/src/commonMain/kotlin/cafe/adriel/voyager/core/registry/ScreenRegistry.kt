package cafe.adriel.voyager.core.registry

import cafe.adriel.voyager.core.concurrent.ThreadSafeMap
import cafe.adriel.voyager.core.platform.multiplatformName
import cafe.adriel.voyager.core.screen.Screen
import kotlin.reflect.KClass

private typealias ProviderKey = KClass<out ScreenProvider>

private typealias ScreenFactory = (ScreenProvider) -> Screen

public object ScreenRegistry {

    @PublishedApi
    internal val factories: ThreadSafeMap<ProviderKey, ScreenFactory> = ThreadSafeMap()

    public operator fun invoke(block: ScreenRegistry.() -> Unit) {
        this.block()
    }

    public inline fun <reified T : ScreenProvider> register(noinline factory: (T) -> Screen) {
        @Suppress("UNCHECKED_CAST")
        factories[T::class] = factory as ScreenFactory
    }

    public fun get(provider: ScreenProvider): Screen {
        val factory = factories[provider::class]
            ?: error("ScreenProvider not registered: ${provider::class.multiplatformName}")
        return factory(provider)
    }
}
