package cafe.adriel.voyager.navigator

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.SaveableStateHolder
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.runtime.staticCompositionLocalOf
import cafe.adriel.voyager.core.concurrent.ThreadSafeSet
import cafe.adriel.voyager.core.lifecycle.ScreenLifecycleStore
import cafe.adriel.voyager.core.lifecycle.rememberScreenLifecycleOwner
import cafe.adriel.voyager.core.model.ScreenModelStore
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.stack.Stack
import cafe.adriel.voyager.core.stack.toMutableStateStack
import cafe.adriel.voyager.navigator.internal.LifecycleDisposableEffect
import cafe.adriel.voyager.navigator.internal.LocalNavigatorStateHolder
import cafe.adriel.voyager.navigator.internal.NavigatorBackHandler
import cafe.adriel.voyager.navigator.internal.NavigatorDisposableEffect
import cafe.adriel.voyager.navigator.internal.StepDisposableEffect
import cafe.adriel.voyager.navigator.internal.rememberNavigator

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

    navigator.saveableState("currentScreen") {
        currentScreen.Content()
    }
}

@Composable
public fun Navigator(
    screen: Screen,
    disposeBehavior: NavigatorDisposeBehavior = NavigatorDisposeBehavior(),
    onBackPressed: OnBackPressed = { true },
    content: NavigatorContent = { CurrentScreen() }
) {
    Navigator(
        screens = listOf(screen),
        disposeBehavior = disposeBehavior,
        onBackPressed = onBackPressed,
        content = content
    )
}

@Composable
public fun Navigator(
    screens: List<Screen>,
    disposeBehavior: NavigatorDisposeBehavior = NavigatorDisposeBehavior(),
    onBackPressed: OnBackPressed = { true },
    content: NavigatorContent = { CurrentScreen() }
) {
    require(screens.isNotEmpty()) { "Navigator must have at least one screen" }

    CompositionLocalProvider(
        LocalNavigatorStateHolder providesDefault rememberSaveableStateHolder()
    ) {
        val navigator = rememberNavigator(screens, disposeBehavior, LocalNavigator.current)

        if (navigator.parent?.disposeBehavior?.disposeNestedNavigators != false) {
            NavigatorDisposableEffect(navigator)
        }

        CompositionLocalProvider(
            LocalNavigator provides navigator,
        ) {
            if (disposeBehavior.disposeSteps) {
                StepDisposableEffect(navigator)
            }

            NavigatorBackHandler(navigator, onBackPressed)

            content(navigator)
        }
    }
}

public class Navigator internal constructor(
    screens: List<Screen>,
    private val stateHolder: SaveableStateHolder,
    public val disposeBehavior: NavigatorDisposeBehavior,
    public val parent: Navigator? = null
) : Stack<Screen> by screens.toMutableStateStack(minSize = 1) {

    public val level: Int =
        parent?.level?.inc() ?: 0

    public val lastItem: Screen by derivedStateOf {
        lastItemOrNull ?: error("Navigator has no screen")
    }

    private val stateKeys = ThreadSafeSet<String>()

    @Deprecated(
        message = "Use 'lastItem' instead. Will be removed in 1.0.0.",
        replaceWith = ReplaceWith("lastItem")
    )
    public val last: Screen by derivedStateOf {
        lastItem
    }

    @Composable
    public fun saveableState(
        key: String,
        screen: Screen = lastItem,
        content: @Composable () -> Unit
    ) {
        val stateKey = "${screen.key}:$key"
        stateKeys += stateKey

        val lifecycleOwner = rememberScreenLifecycleOwner(screen)
        val lifecycleKey = "$stateKey:lifecycle"
        stateKeys += lifecycleKey
        stateHolder.SaveableStateProvider(lifecycleKey) {
            LifecycleDisposableEffect(lifecycleOwner)

            val hooks = lifecycleOwner.getHooks()

            CompositionLocalProvider(*hooks.providers.toTypedArray()) {
                stateHolder.SaveableStateProvider(stateKey, content = content)
            }
        }
    }

    public fun popUntilRoot() {
        popUntilRoot(this)
    }

    private tailrec fun popUntilRoot(navigator: Navigator) {
        navigator.popAll()

        if (navigator.parent != null) {
            popUntilRoot(navigator.parent)
        }
    }

    internal fun dispose(
        screen: Screen
    ) {
        ScreenModelStore.remove(screen)
        ScreenLifecycleStore.remove(screen)
        stateKeys
            .asSequence()
            .filter { it.startsWith(screen.key) }
            .forEach { key ->
                stateHolder.removeState(key)
                stateKeys -= key
            }
    }
}

public data class NavigatorDisposeBehavior(
    val disposeNestedNavigators: Boolean = true,
    val disposeSteps: Boolean = true,
)
