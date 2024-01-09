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

    override val lifecycle: LifecycleRegistry = LifecycleRegistry(this)

    override val viewModelStore: ViewModelStore = ViewModelStore()

    private val atomicContext = AtomicReference<Context>()
    internal val atomicParentLifecycleOwner = AtomicReference<LifecycleOwner>()

    private val controller = SavedStateRegistryController.create(this)

    private var isCreated: Boolean by mutableStateOf(false)

    override val savedStateRegistry: SavedStateRegistry
        get() = controller.savedStateRegistry

    override val defaultViewModelProviderFactory: ViewModelProvider.Factory
        get() = SavedStateViewModelFactory(
            application = atomicContext.get()?.applicationContext?.getApplication(),
            owner = this
        )

    override val defaultViewModelCreationExtras: CreationExtras
        get() = MutableCreationExtras().apply {
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

    init {
        controller.performAttach()
        enableSavedStateHandles()
    }

    private fun onCreate(savedState: Bundle?) {
        check(!isCreated) { "onCreate already called" }
        isCreated = true
        controller.performRestore(savedState)
        initEvents.forEach {
            lifecycle.safeHandleLifecycleEvent(it)
        }
    }

    private fun emitOnStartEvents() {
        startEvents.forEach {
            lifecycle.safeHandleLifecycleEvent(it)
        }
    }

    private fun emitOnStopEvents() {
        stopEvents.forEach {
            lifecycle.safeHandleLifecycleEvent(it)
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
        val activity = context.getActivity()
        if (activity != null && activity.isChangingConfigurations) return
        viewModelStore.clear()
        disposeEvents.forEach { event ->
            lifecycle.safeHandleLifecycleEvent(event)
        }
    }

    private fun performSave(outState: Bundle) {
        controller.performSave(outState)
    }

    @Composable
    private fun getHooks(): List<ProvidedValue<*>> {
        atomicContext.compareAndSet(null, LocalContext.current)
        atomicParentLifecycleOwner.compareAndSet(null, LocalLifecycleOwner.current)

        return remember(this) {
            listOf(
                LocalLifecycleOwner provides this,
                LocalViewModelStoreOwner provides this,
                LocalSavedStateRegistryOwner provides this
            )
        }
    }

    /**
     * Returns a unregister callback
     */
    private fun registerLifecycleListener(outState: Bundle): () -> Unit {
        val lifecycleOwner = atomicParentLifecycleOwner.get()
        if (lifecycleOwner != null) {
            val observer = object : DefaultLifecycleObserver {
                override fun onPause(owner: LifecycleOwner) {
                    lifecycle.safeHandleLifecycleEvent(Lifecycle.Event.ON_PAUSE)
                }

                override fun onResume(owner: LifecycleOwner) {
                    lifecycle.safeHandleLifecycleEvent(Lifecycle.Event.ON_RESUME)
                }

                override fun onStart(owner: LifecycleOwner) {
                    lifecycle.safeHandleLifecycleEvent(Lifecycle.Event.ON_START)
                }

                override fun onStop(owner: LifecycleOwner) {
                    lifecycle.safeHandleLifecycleEvent(Lifecycle.Event.ON_STOP)

                    // when the Application goes to background, perform save
                    performSave(outState)
                }
            }
            val lifecycle = lifecycleOwner.lifecycle
            lifecycle.addObserver(observer)

            return { lifecycle.removeObserver(observer) }
        } else {
            return { }
        }
    }

    @Composable
    private fun LifecycleDisposableEffect() {
        val savedState = rememberSaveable { Bundle() }
        if (!isCreated) {
            onCreate(savedState) // do this in the UI thread to force it to be called before anything else
        }

        DisposableEffect(this) {
            val unregisterLifecycle = registerLifecycleListener(savedState)
            emitOnStartEvents()

            onDispose {
                unregisterLifecycle()

                // when the screen goes to stack, perform save
                performSave(savedState)

                // notify lifecycle screen listeners
                emitOnStopEvents()
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

    private fun LifecycleRegistry.safeHandleLifecycleEvent(event: Lifecycle.Event) {
        val currentState = currentState
        if (!currentState.isAtLeast(Lifecycle.State.INITIALIZED)) return

        handleLifecycleEvent(event)
    }

    public companion object {

        private val initEvents = arrayOf(
            Lifecycle.Event.ON_CREATE
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
            return ScreenLifecycleStore.get(screen) { AndroidScreenLifecycleOwner() }
        }
    }
}
