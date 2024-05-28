package cafe.adriel.voyager.core.lifecycle

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.core.bundle.Bundle
import androidx.lifecycle.AtomicReference
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.savedstate.SavedStateRegistry
import androidx.savedstate.SavedStateRegistryController
import androidx.savedstate.SavedStateRegistryOwner
import cafe.adriel.voyager.core.annotation.InternalVoyagerApi
import cafe.adriel.voyager.core.screen.Screen

public interface ScreenLifecycleOwner : ScreenLifecycleContentProvider,
    LifecycleOwner,
    SavedStateRegistryOwner,
    ScreenDisposable

public interface ScreenLifecycleContentProvider {
    /**
     * Called before rendering the Screen Content.
     *
     * IMPORTANT: This is only called when ScreenLifecycleOwner is provided by [ScreenLifecycleProvider] or [NavigatorScreenLifecycleProvider].
     */
    @Composable
    public fun ProvideBeforeScreenContent(
        provideSaveableState: @Composable (suffixKey: String, content: @Composable () -> Unit) -> Unit,
        content: @Composable () -> Unit
    ): Unit = content()
}

public interface ScreenDisposable {
    /**
     * Called on the Screen leaves the stack.
     */
    public fun onDispose(screen: Screen) {}
}

public abstract class CommonScreenLifecycleOwner : ScreenLifecycleOwner {

    private val lifecycleRegistry by lazy(LazyThreadSafetyMode.NONE) {
        LifecycleRegistry(this)
    }

    override val lifecycle: Lifecycle
        get() = lifecycleRegistry

    private val controller by lazy(LazyThreadSafetyMode.NONE) {
        SavedStateRegistryController.create(this)
    }

    override val savedStateRegistry: SavedStateRegistry
        get() = controller.savedStateRegistry

    private var isCreated: Boolean by mutableStateOf(false)

    internal val atomicParentLifecycleOwner = AtomicReference<LifecycleOwner?>(null)

    init {
        controller.performAttach()
    }

    @Composable
    public fun LifecycleDisposableEffect() {
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

    /**
     * Returns a unregister callback
     */
    private fun registerLifecycleListener(outState: Bundle): () -> Unit {
        val lifecycleOwner = atomicParentLifecycleOwner.get()
        if (lifecycleOwner != null) {
            val observer = object : DefaultLifecycleObserver {
                override fun onPause(owner: LifecycleOwner) {
                    lifecycleRegistry.safeHandleLifecycleEvent(Lifecycle.Event.ON_PAUSE)
                }

                override fun onResume(owner: LifecycleOwner) {
                    lifecycleRegistry.safeHandleLifecycleEvent(Lifecycle.Event.ON_RESUME)
                }

                override fun onStart(owner: LifecycleOwner) {
                    lifecycleRegistry.safeHandleLifecycleEvent(Lifecycle.Event.ON_START)
                }

                override fun onStop(owner: LifecycleOwner) {
                    lifecycleRegistry.safeHandleLifecycleEvent(Lifecycle.Event.ON_STOP)

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

    private fun performSave(outState: Bundle) {
        controller.performSave(outState)
    }


    private fun onCreate(savedState: Bundle?) {
        check(!isCreated) { "onCreate already called" }
        isCreated = true
        controller.performRestore(savedState)
        initEvents.forEach {
            lifecycleRegistry.safeHandleLifecycleEvent(it)
        }
    }

    private fun emitOnStartEvents() {
        startEvents.forEach {
            lifecycleRegistry.safeHandleLifecycleEvent(it)
        }
    }

    private fun emitOnStopEvents() {
        stopEvents.forEach {
            lifecycleRegistry.safeHandleLifecycleEvent(it)
        }
    }


    private fun LifecycleRegistry.safeHandleLifecycleEvent(event: Lifecycle.Event) {
        val currentState = currentState
        if (!currentState.isAtLeast(Lifecycle.State.INITIALIZED)) return

        handleLifecycleEvent(event)
    }

    override fun onDispose(screen: Screen) {
        disposeEvents.forEach { event ->
            lifecycleRegistry.safeHandleLifecycleEvent(event)
        }
    }

    private companion object {
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
    }
}


@InternalVoyagerApi
public object DefaultScreenLifecycleOwner : CommonScreenLifecycleOwner()
