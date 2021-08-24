package cafe.adriel.voyager.core.registry

import cafe.adriel.voyager.core.screen.Screen
import java.util.concurrent.ConcurrentHashMap
import kotlin.reflect.KClass

private typealias ProviderKey = KClass<out ScreenProvider>

private typealias ScreenFactory = (ScreenProvider) -> Screen

public object ScreenRegistry {

    @PublishedApi
    internal val factories: ConcurrentHashMap<ProviderKey, ScreenFactory> = ConcurrentHashMap()

    public operator fun invoke(block: ScreenRegistry.() -> Unit) {
        this.block()
    }

    public inline fun <reified T : ScreenProvider> register(noinline factory: (T) -> Screen) {
        factories[T::class] = factory as ScreenFactory
    }

    public inline fun <reified T : ScreenProvider> get(provider: T): Screen {
        val factory = factories[T::class] ?: error("ScreenProvider not registered: ${T::class.qualifiedName}")
        return factory(provider)
    }
}
