package cafe.adriel.voyager

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.currentCompositeKeyHash
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.staticCompositionLocalOf
import cafe.adriel.voyager.internal.LocalNavigatorCache
import cafe.adriel.voyager.internal.NavigatorBackHandler
import cafe.adriel.voyager.internal.NavigatorCache
import cafe.adriel.voyager.internal.OnBackPressed
import cafe.adriel.voyager.internal.rememberNavigator
import cafe.adriel.voyager.stack.Stack
import cafe.adriel.voyager.stack.toMutableStateStack

public typealias NavigatorContent = @Composable (navigator: Navigator) -> Unit

public val LocalNavigator: ProvidableCompositionLocal<Navigator?> =
    staticCompositionLocalOf { null }

public val <T> ProvidableCompositionLocal<T?>.currentOrThrow: T
    @Composable
    get() = current ?: error("CompositionLocal is null")

@Composable
public fun Navigator(
    screen: Screen,
    onBackPressed: OnBackPressed = { true },
    content: NavigatorContent = { CurrentScreen() }
) {
    Navigator(
        screens = listOf(screen),
        onBackPressed = onBackPressed,
        content = content
    )
}

@Composable
public fun Navigator(
    screens: List<Screen>,
    onBackPressed: OnBackPressed = { true },
    content: NavigatorContent = { CurrentScreen() }
) {
    require(screens.isNotEmpty()) { "Navigator must have at least one screen" }

    NavigatorCache {
        val keyHash = currentCompositeKeyHash
        val navigatorCache = LocalNavigatorCache.current
        val currentScreens = navigatorCache[keyHash] ?: screens
        val navigator = rememberNavigator(
            screens = currentScreens,
            parent = LocalNavigator.current
        )

        CompositionLocalProvider(
            LocalNavigator provides navigator
        ) {
            NavigatorBackHandler(onBackPressed)
            content(navigator)
        }

        DisposableEffect(keyHash) {
            navigatorCache -= keyHash
            onDispose { navigatorCache[keyHash] = navigator.items }
        }
    }
}

public class Navigator internal constructor(
    screens: List<Screen>,
    public val parent: Navigator? = null
) : Stack<Screen> by screens.toMutableStateStack(minSize = 1) {

    public val level: Int =
        parent?.level?.inc() ?: 0

    public val last: Screen by derivedStateOf {
        lastOrNull ?: error("Navigator has no screen")
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
