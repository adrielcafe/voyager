package cafe.adriel.voyager.androidx

import android.app.Activity
import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
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
import java.util.concurrent.atomic.AtomicReference

public class AndroidScreenLifecycleOwner private constructor() :
    ScreenLifecycleOwner,
    LifecycleOwner,
    ViewModelStoreOwner,
    SavedStateRegistryOwner {

    private val registry = LifecycleRegistry(this)

    private val store = ViewModelStore()

    private val controller = SavedStateRegistryController.create(this)

    private val atomicContext = AtomicReference<Context>()

    init {
        if (controller.savedStateRegistry.isRestored.not()) {
            controller.performRestore(null)
        }
        initStates.forEach { registry.currentState = it }
    }

    override fun onDispose(screen: Screen) {
        val context = atomicContext.getAndSet(null) ?: return
        if (context is Activity && context.isChangingConfigurations) return
        viewModelStore.clear()
        disposeStates.forEach { registry.currentState = it }
    }

    @Composable
    override fun getHooks(): ScreenLifecycleHooks {
        atomicContext.compareAndSet(null, LocalContext.current)

        return remember(this) {
            ScreenLifecycleHooks(
                providers = listOf(
                    LocalLifecycleOwner provides this,
                    LocalViewModelStoreOwner provides this,
                    LocalSavedStateRegistryOwner provides this,
                )
            )
        }
    }

    override fun getLifecycle(): Lifecycle = registry

    override fun getViewModelStore(): ViewModelStore = store

    override fun getSavedStateRegistry(): SavedStateRegistry = controller.savedStateRegistry

    public companion object {

        private val initStates = arrayOf(
            Lifecycle.State.INITIALIZED,
            Lifecycle.State.CREATED,
            Lifecycle.State.STARTED,
            Lifecycle.State.RESUMED
        )

        private val disposeStates = arrayOf(
            Lifecycle.State.DESTROYED
        )

        public fun get(screen: Screen): ScreenLifecycleOwner =
            ScreenLifecycleStore.get(screen) { AndroidScreenLifecycleOwner() }
    }
}
