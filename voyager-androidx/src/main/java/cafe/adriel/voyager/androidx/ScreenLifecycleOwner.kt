package cafe.adriel.voyager.androidx

import androidx.compose.ui.platform.LocalSavedStateRegistryOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.savedstate.SavedStateRegistry
import androidx.savedstate.SavedStateRegistryController
import androidx.savedstate.SavedStateRegistryOwner
import cafe.adriel.voyager.core.hook.ScreenHook

public val ScreenLifecycleOwner.screenLifecycleHooks: List<ScreenHook>
    get() = listOf(
        ScreenHook.OnProvide { LocalViewModelStoreOwner provides this },
        ScreenHook.OnProvide { LocalSavedStateRegistryOwner provides this },
        ScreenHook.OnDispose { viewModelStore.clear() }
    )

public interface ScreenLifecycleOwner : LifecycleOwner, ViewModelStoreOwner, SavedStateRegistryOwner

public class ScreenLifecycleHolder : ScreenLifecycleOwner {

    private val store = ViewModelStore()

    private val registry = LifecycleRegistry(this)

    private val controller = SavedStateRegistryController.create(this)

    init {
        controller.performRestore(null)
    }

    override fun getViewModelStore(): ViewModelStore = store

    override fun getLifecycle(): Lifecycle = registry

    override fun getSavedStateRegistry(): SavedStateRegistry = controller.savedStateRegistry
}
