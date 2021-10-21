package cafe.adriel.voyager.navigator

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.SaveableStateHolder
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.runtime.staticCompositionLocalOf
import cafe.adriel.voyager.core.lifecycle.ScreenLifecycleStore
import cafe.adriel.voyager.core.lifecycle.rememberScreenLifecycleOwner
import cafe.adriel.voyager.core.model.ScreenModelStore
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.stack.Stack
import cafe.adriel.voyager.core.stack.toMutableStateStack
import cafe.adriel.voyager.navigator.internal.LocalNavigatorStateHolder
import cafe.adriel.voyager.navigator.internal.NavigatorBackHandler
import cafe.adriel.voyager.navigator.internal.NavigatorDisposableEffect
import cafe.adriel.voyager.navigator.internal.rememberNavigator
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicBoolean

public typealias NavigatorContent = @Composable (navigator: Navigator) -> Unit

public typealias OnBackPressed = ((currentScreen: Screen) -> Boolean)?

public val LocalNavigator: ProvidableCompositionLocal<Navigator?> =
    staticCompositionLocalOf { null }

public val <T> ProvidableCompositionLocal<T?>.currentOrThrow: T
    @Composable
    get() = current ?: error("CompositionLocal is null")

@Composable
public fun CurrentScreen() {
    val navigator = LocalNavigator.currentOrThrow
    val currentScreen = navigator.lastItem

    navigator.stateHolder.SaveableStateProvider(currentScreen.key) {
        currentScreen.Content()
    }
}

@Composable
public fun Navigator(
    screen: Screen,
    autoDispose: Boolean = true,
    onBackPressed: OnBackPressed = { true },
    content: NavigatorContent = { CurrentScreen() }
) {
    Navigator(
        screens = listOf(screen),
        autoDispose = autoDispose,
        onBackPressed = onBackPressed,
        content = content
    )
}

@Composable
public fun Navigator(
    screens: List<Screen>,
    autoDispose: Boolean = true,
    onBackPressed: OnBackPressed = { true },
    content: NavigatorContent = { CurrentScreen() }
) {
    require(screens.isNotEmpty()) { "Navigator must have at least one screen" }

    CompositionLocalProvider(
        LocalNavigatorStateHolder providesDefault rememberSaveableStateHolder()
    ) {
        val navigator = rememberNavigator(screens, LocalNavigator.current)
        val currentScreen = navigator.lastItem
        val lifecycleOwner = rememberScreenLifecycleOwner(currentScreen)
        val hooks = lifecycleOwner.getHooks()

        CompositionLocalProvider(
            LocalNavigator provides navigator,
            *hooks.providers.toTypedArray()
        ) {
            // Each screen will have a transition callback by default
            val transitionCallback = remember(currentScreen.key) {
                TransitionCallbackImpl(onDispose = hooks.onDispose).apply {
                    navigator.registerTransitionCallback(currentScreen, this)
                }
            }

            // We don't need concurrent disposable when we have transitions
            if (autoDispose && navigator.hasTransitionCallback.not()) {
                NavigatorDisposableEffect(navigator, transitionCallback)
            }

            NavigatorBackHandler(navigator, onBackPressed)

            content(navigator)
        }
    }
}

public class Navigator internal constructor(
    screens: List<Screen>,
    public val stateHolder: SaveableStateHolder,
    public val parent: Navigator? = null
) : Stack<Screen> by screens.toMutableStateStack(minSize = 1) {
    private val transitionCallbacks = ConcurrentHashMap<Screen, TransitionCallback>()
    private val registeredTransitions = AtomicBoolean(false)

    internal fun registerTransitionCallback(screen: Screen, transitionCallback: TransitionCallback) {
        transitionCallbacks[screen] = transitionCallback
    }

    internal fun unregisterTransitionCallback(screen: Screen) {
        transitionCallbacks.remove(screen)
    }

    public val level: Int =
        parent?.level?.inc() ?: 0

    public val lastItem: Screen by derivedStateOf {
        lastItemOrNull ?: error("Navigator has no screen")
    }

    @Deprecated(
        message = "Use 'lastItem' instead. Will be removed in 1.0.0.",
        replaceWith = ReplaceWith("lastItem")
    )
    public val last: Screen by derivedStateOf {
        lastItem
    }

    // TODO: it would be nice don't have variables and functions public related to transitions
    public val hasTransitionCallback: Boolean
        get() = registeredTransitions.get()

    public fun notifyTransitionEnd(previous: Screen) {
        val callback = transitionCallbacks.remove(previous)
        callback?.onTransitionEnd(navigator = this, previous = previous)
    }

    public fun registerToDisposeAfterTransitionEnd(screenToDispose: Screen) {
        registeredTransitions.compareAndSet(false, transitionCallbacks.containsKey(screenToDispose))
    }

    public fun popUntilRoot() {
        popUntilRoot(this)
    }

    private tailrec fun popUntilRoot(navigator: Navigator): Navigator {
        navigator.popAll()

        return if (navigator.parent == null) {
            navigator
        } else {
            popUntilRoot(navigator.parent)
        }
    }
}

/**
 * A fallback when a screen was replaced by other
 */
internal interface TransitionCallback {
    /**
     * Called when previous screen is totally invisible
     *
     * @param navigator The navigator in the current transition
     * @param previous The screen that was popped/replaced
     */
    fun onTransitionEnd(navigator: Navigator, previous: Screen)
}

internal data class TransitionCallbackImpl(
    private val onDispose: () -> Unit
) : TransitionCallback {
    override fun onTransitionEnd(navigator: Navigator, previous: Screen) {
        // TODO: Maybe dispose operations should be called inside remove operations instead here
        onDispose()
        ScreenModelStore.remove(previous)
        ScreenLifecycleStore.remove(previous)
        navigator.stateHolder.removeState(previous.key)
        navigator.clearEvent()
        navigator.unregisterTransitionCallback(previous)
    }
}
