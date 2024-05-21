package cafe.adriel.voyager.core.lifecycle

import cafe.adriel.voyager.core.concurrent.ThreadSafeMap
import cafe.adriel.voyager.core.concurrent.ThreadSafeSet
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey

internal object LifecycleEffectStore : ScreenDisposable {
    private val executedLifecycles = ThreadSafeMap<ScreenKey, ThreadSafeSet<String>>()

    fun store(screen: Screen, effectKey: String) {
        val set = executedLifecycles.getOrPut(screen.key) { ThreadSafeSet() }
        set.add(effectKey)
    }

    fun hasExecuted(screen: Screen, effectKey: String): Boolean =
        executedLifecycles.get(screen.key)?.contains(effectKey) == true

    override fun onDispose(screen: Screen) {
        executedLifecycles.remove(screen.key)
    }
}
