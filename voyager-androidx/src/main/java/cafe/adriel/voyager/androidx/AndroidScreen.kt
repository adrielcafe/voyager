package cafe.adriel.voyager.androidx

import androidx.lifecycle.ViewModelStoreOwner
import cafe.adriel.voyager.core.hook.HookableScreen
import cafe.adriel.voyager.core.hook.ScreenHookHandler
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.uniqueScreenKey

public abstract class AndroidScreen :
    Screen,
    HookableScreen by ScreenHookHandler(),
    ViewModelStoreOwner by ScreenViewModelStoreOwner() {

    init {
        addHooks(viewModelScreenHooks)
    }

    override val key: String = uniqueScreenKey
}
