package cafe.adriel.voyager.androidx

import cafe.adriel.voyager.core.hook.HookableScreen
import cafe.adriel.voyager.core.hook.ScreenHookHandler
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.uniqueScreenKey

public abstract class AndroidScreen :
    Screen,
    HookableScreen by ScreenHookHandler(),
    ScreenLifecycleOwner by ScreenLifecycleHolder() {

    init {
        addHooks(screenLifecycleHooks)
    }

    override val key: String = uniqueScreenKey
}
