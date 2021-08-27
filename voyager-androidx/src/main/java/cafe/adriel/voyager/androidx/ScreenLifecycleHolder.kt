package cafe.adriel.voyager.androidx

import android.app.Activity
import android.content.Context
import androidx.compose.runtime.Composable
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
import cafe.adriel.voyager.core.lifecycle.ScreenHooks
import cafe.adriel.voyager.core.lifecycle.ScreenLifecycleOwner
import cafe.adriel.voyager.core.screen.ScreenKey
import java.util.concurrent.ConcurrentHashMap

public class ScreenLifecycleHolder private constructor(
    private val key: ScreenKey
) : ScreenLifecycleOwner,
    LifecycleOwner,
    ViewModelStoreOwner,
    SavedStateRegistryOwner {

    private val registry = LifecycleRegistry(this)

    private val store = ViewModelStore()

    private val controller = SavedStateRegistryController.create(this)

    private val Context.canDispose: Boolean
        get() = (this as? Activity)?.isChangingConfigurations?.not() ?: true

    init {
        controller.performRestore(null)
    }

    @Composable
    override fun getHooks(): ScreenHooks {
        val context = LocalContext.current

        return ScreenHooks(
            providers = listOf(
                LocalViewModelStoreOwner provides this,
                LocalSavedStateRegistryOwner provides this,
            ),
            disposer = {
                if (context.canDispose) {
                    viewModelStore.clear()
                    remove(key)
                }
            }
        )
    }

    override fun getLifecycle(): Lifecycle = registry

    override fun getViewModelStore(): ViewModelStore = store

    override fun getSavedStateRegistry(): SavedStateRegistry = controller.savedStateRegistry

    internal companion object {

        private val holders = ConcurrentHashMap<ScreenKey, ScreenLifecycleHolder>()

        internal fun get(key: ScreenKey) =
            holders.getOrPut(key) { ScreenLifecycleHolder(key) }

        private fun remove(key: ScreenKey) {
            holders -= key
        }
    }
}
