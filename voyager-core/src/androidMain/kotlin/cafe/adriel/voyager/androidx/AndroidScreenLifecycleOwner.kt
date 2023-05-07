package cafe.adriel.voyager.androidx

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.ContextWrapper
import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.ProvidedValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.LocalSavedStateRegistryOwner
import androidx.lifecycle.DefaultLifecycleObserver
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
import androidx.lifecycle.enableSavedStateHandles
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.MutableCreationExtras
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.savedstate.SavedStateRegistry
import androidx.savedstate.SavedStateRegistryController
import androidx.savedstate.SavedStateRegistryOwner
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

    private val atomicContext = AtomicReference<Context>()

    private val controller = SavedStateRegistryController.create(this)

    private var deactivateLifecycleListener: (() -> Unit)? = null

    private var isCreated: Boolean by mutableStateOf(false)

    override val savedStateRegistry: SavedStateRegistry
        get() = controller.savedStateRegistry

    init {
        controller.performAttach()
        enableSavedStateHandles()
    }

    private fun onCreate(savedState: Bundle?) {
        check(!isCreated) { "onCreate already called" }
        isCreated = true
        controller.performRestore(savedState)
        initEvents.forEach {
            registry.handleLifecycleEvent(it)
        }
    }

    private fun onStart() {
        startEvents.forEach {
            registry.handleLifecycleEvent(it)
        }
    }

    private fun onStop() {
        deactivateLifecycleListener?.invoke()
        deactivateLifecycleListener = null
        stopEvents.forEach {
            registry.handleLifecycleEvent(it)
        }
    }

    @Composable
    override fun ProvideBeforeScreenContent(
        provideSaveableState: @Composable (suffixKey: String, content: @Composable () -> Unit) -> Unit,
        content: @Composable () -> Unit
    ) {
        provideSaveableState("lifecycle") {
            LifecycleDisposableEffect()

            val hooks = getHooks()

            CompositionLocalProvider(*hooks.toTypedArray()) {
                content()
            }
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

    private fun performSave(outState: Bundle) {
        controller.performSave(outState)
    }

    @Composable
    private fun getHooks(): List<ProvidedValue<*>> {
        atomicContext.compareAndSet(null, LocalContext.current)

        return remember(this) {
            listOf(
                LocalLifecycleOwner provides this,
                LocalViewModelStoreOwner provides this,
                LocalSavedStateRegistryOwner provides this,
            )
        }
    }

    override fun getLifecycle(): Lifecycle = registry

    override fun getViewModelStore(): ViewModelStore = store

    override fun getDefaultViewModelProviderFactory(): ViewModelProvider.Factory {
        return SavedStateViewModelFactory(
            application = atomicContext.get()?.applicationContext?.getApplication(),
            owner = this
        )
    }

    private fun registerLifecycleListener(outState: Bundle) {
        val activity = atomicContext.get()?.getActivity()
        if (activity != null && activity is LifecycleOwner) {
            val observer = object : DefaultLifecycleObserver {
                override fun onStop(owner: LifecycleOwner) {
                    performSave(outState)
                }
            }
            val lifecycle = activity.lifecycle
            lifecycle.addObserver(observer)
            deactivateLifecycleListener = { lifecycle.removeObserver(observer) }
        }
    }

    override fun getDefaultViewModelCreationExtras(): CreationExtras = MutableCreationExtras().apply {
        val application = atomicContext.get()?.applicationContext?.getApplication()
        if (application != null) {
            set(AndroidViewModelFactory.APPLICATION_KEY, application)
        }
        set(SAVED_STATE_REGISTRY_OWNER_KEY, this@AndroidScreenLifecycleOwner)
        set(VIEW_MODEL_STORE_OWNER_KEY, this@AndroidScreenLifecycleOwner)

        /* TODO if (getArguments() != null) {
            extras.set<Bundle>(DEFAULT_ARGS_KEY, getArguments())
        }*/
    }

    @Composable
    private fun LifecycleDisposableEffect() {
        val savedState = rememberSaveable { Bundle() }
        if (!isCreated) {
            onCreate(savedState) // do this in the UI thread to force it to be called before anything else
        }

        DisposableEffect(this) {
            registerLifecycleListener(savedState)
            onStart()
            onDispose {
                performSave(savedState)
                onStop()
            }
        }
    }

    private tailrec fun Context.getActivity(): Activity? = when (this) {
        is Activity -> this
        is ContextWrapper -> baseContext.getActivity()
        else -> null
    }

    private tailrec fun Context.getApplication(): Application? = when (this) {
        is Application -> this
        is ContextWrapper -> baseContext.getApplication()
        else -> null
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
        public fun get(screen: Screen): ScreenLifecycleOwner {
            return ScreenLifecycleStore.register(screen) { AndroidScreenLifecycleOwner() }
        }

    }
}
