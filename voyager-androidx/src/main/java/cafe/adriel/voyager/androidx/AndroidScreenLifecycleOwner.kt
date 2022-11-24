package cafe.adriel.voyager.androidx

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.ContextWrapper
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.LocalSavedStateRegistryOwner
import androidx.lifecycle.HasDefaultViewModelProviderFactory
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.SAVED_STATE_REGISTRY_OWNER_KEY
import androidx.lifecycle.SavedStateViewModelFactory
import androidx.lifecycle.VIEW_MODEL_STORE_OWNER_KEY
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.MutableCreationExtras
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
    SavedStateRegistryOwner,
    HasDefaultViewModelProviderFactory {

    private val registry = LifecycleRegistry(this)

    private val store = ViewModelStore()

    private val controller = SavedStateRegistryController.create(this)

    private val atomicContext = AtomicReference<Context>()

    init {
        if (controller.savedStateRegistry.isRestored.not()) {
            controller.performRestore(null)
        }
        initEvents.forEach {
            registry.handleLifecycleEvent(it)
        }
    }

    override fun onStart() {
        startEvents.forEach {
            registry.handleLifecycleEvent(it)
        }
    }

    override fun onStop() {
        stopEvents.forEach {
            registry.handleLifecycleEvent(it)
        }
    }

    override fun onDispose(screen: Screen) {
        val context = atomicContext.getAndSet(null) ?: return
        if (context is Activity && context.isChangingConfigurations) return
        viewModelStore.clear()
        disposeEvents.forEach {
            registry.handleLifecycleEvent(it)
        }
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

    override fun getDefaultViewModelProviderFactory(): ViewModelProvider.Factory {
        return SavedStateViewModelFactory(
            (atomicContext.get()?.applicationContext as? Application),
            this
        )
    }

    override fun getDefaultViewModelCreationExtras(): CreationExtras = MutableCreationExtras().apply {
        var application: Application? = null
        var context = atomicContext.get()?.applicationContext
        while (context is ContextWrapper) {
            if (context is Application) {
                application = context
                break
            }
            context = context.baseContext
        }
        if (application != null) {
            set(AndroidViewModelFactory.APPLICATION_KEY, application)
        }
        set(SAVED_STATE_REGISTRY_OWNER_KEY, this@AndroidScreenLifecycleOwner)
        set(VIEW_MODEL_STORE_OWNER_KEY, this@AndroidScreenLifecycleOwner)

        /* TODO if (getArguments() != null) {
            extras.set<Bundle>(DEFAULT_ARGS_KEY, getArguments())
        }*/
    }

    public companion object {

        private val initEvents = arrayOf(
            Lifecycle.Event.ON_CREATE,
        )

        private val startEvents = arrayOf(
            Lifecycle.Event.ON_START,
            Lifecycle.Event.ON_RESUME
        )

        private val stopEvents = arrayOf(
            Lifecycle.Event.ON_PAUSE,
            Lifecycle.Event.ON_STOP
        )

        private val disposeEvents = arrayOf(
            Lifecycle.Event.ON_DESTROY
        )

        public fun get(screen: Screen): ScreenLifecycleOwner =
            ScreenLifecycleStore.get(screen) { AndroidScreenLifecycleOwner() }
    }
}
