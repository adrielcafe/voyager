package cafe.adriel.voyager.navigator

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
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
        val lifecycleOwner = rememberScreenLifecycleOwner(navigator.lastItem)
        val hooks = lifecycleOwner.getHooks()

        DisposableEffect(navigator) {
            onDispose {
                for (screen in navigator.items) {
                    ScreenModelStore.remove(screen)
                    ScreenLifecycleStore.remove(screen)
                    navigator.stateHolder.removeState(screen.key)
                }
                navigator.clearEvent()
            }
        }

        CompositionLocalProvider(
            LocalNavigator provides navigator,
            *hooks.providers.toTypedArray()
        ) {
            if (autoDispose) {
                NavigatorDisposableEffect(navigator)
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
