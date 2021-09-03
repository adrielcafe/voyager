package cafe.adriel.voyager.androidx

import android.app.Activity
import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
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
import cafe.adriel.voyager.core.lifecycle.ScreenLifecycleHooks
import cafe.adriel.voyager.core.lifecycle.ScreenLifecycleOwner
import cafe.adriel.voyager.core.lifecycle.ScreenLifecycleStore
import cafe.adriel.voyager.core.screen.Screen

public class AndroidScreenLifecycleOwner private constructor() :
    ScreenLifecycleOwner,
    LifecycleOwner,
    ViewModelStoreOwner,
    SavedStateRegistryOwner {

    private val registry = LifecycleRegistry(this)

    private val store = ViewModelStore()

    private val controller = SavedStateRegistryController.create(this)

    private val Context.isChangingConfigurations: Boolean
        get() = (this as? Activity)?.isChangingConfigurations ?: false

    init {
        if (controller.savedStateRegistry.isRestored.not()) {
            controller.performRestore(null)
        }
    }

    @Composable
    override fun getHooks(): ScreenLifecycleHooks {
        val context = LocalContext.current

        return remember(this) {
            ScreenLifecycleHooks(
                providers = listOf(
                    LocalViewModelStoreOwner provides this,
                    LocalSavedStateRegistryOwner provides this,
                ),
                onDispose = {
                    if (context.isChangingConfigurations.not()) {
                        viewModelStore.clear()
                    }
                }
            )
        }
    }

    override fun getLifecycle(): Lifecycle = registry

    override fun getViewModelStore(): ViewModelStore = store

    override fun getSavedStateRegistry(): SavedStateRegistry = controller.savedStateRegistry

    public companion object {

        public fun get(screen: Screen): ScreenLifecycleOwner =
            ScreenLifecycleStore.get(screen) { AndroidScreenLifecycleOwner() }
    }
}
