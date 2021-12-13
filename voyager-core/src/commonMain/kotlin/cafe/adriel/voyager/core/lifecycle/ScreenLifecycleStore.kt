package cafe.adriel.voyager.core.lifecycle

import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import java.util.concurrent.ConcurrentHashMap

public object ScreenLifecycleStore {

    private val owners = ConcurrentHashMap<ScreenKey, ScreenLifecycleOwner>()

    public fun get(
        screen: Screen,
        factory: (ScreenKey) -> ScreenLifecycleOwner
    ): ScreenLifecycleOwner =
        owners.getOrPut(screen.key) { factory(screen.key) }

    public fun remove(screen: Screen) {
        owners -= screen.key
    }
}
