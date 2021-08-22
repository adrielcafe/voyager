package cafe.adriel.voyager.androidx

import androidx.lifecycle.ViewModelStoreOwner
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenHook
import cafe.adriel.voyager.core.screen.uniqueScreenKey

public abstract class AndroidScreen : Screen, ViewModelStoreOwner by ScreenViewModelStoreOwner() {

    override val key: String = uniqueScreenKey

    override val hooks: List<ScreenHook> = viewModelScreenHooks
}
