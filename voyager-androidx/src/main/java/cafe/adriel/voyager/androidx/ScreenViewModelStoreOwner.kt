package cafe.adriel.voyager.androidx

import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import cafe.adriel.voyager.core.screen.ScreenHook

public val ViewModelStoreOwner.viewModelScreenHooks: List<ScreenHook>
    get() = listOf(
        ScreenHook.OnProvide { LocalViewModelStoreOwner provides this },
        ScreenHook.OnDispose { viewModelStore.clear() }
    )

public class ScreenViewModelStoreOwner : ViewModelStoreOwner {

    private val store = ViewModelStore()

    public override fun getViewModelStore(): ViewModelStore = store
}
