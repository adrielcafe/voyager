package cafe.adriel.voyager.navigator

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.currentCompositeKeyHash
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.staticCompositionLocalOf
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.stack.Stack
import cafe.adriel.voyager.core.stack.toMutableStateStack
import cafe.adriel.voyager.navigator.internal.LocalNavigatorCache
import cafe.adriel.voyager.navigator.internal.NavigatorBackHandler
import cafe.adriel.voyager.navigator.internal.NavigatorCache
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
    LocalNavigator.currentOrThrow.last.Content()
}

@Composable
public fun Navigator(
    initialScreen: Screen,
    onBackPressed: OnBackPressed = { true },
    content: NavigatorContent = { CurrentScreen() }
) {
    Navigator(
        initialScreens = listOf(initialScreen),
        onBackPressed = onBackPressed,
        content = content
    )
}

@Composable
public fun Navigator(
    initialScreens: List<Screen>,
    onBackPressed: OnBackPressed = { true },
    content: NavigatorContent = { CurrentScreen() }
) {
    require(initialScreens.isNotEmpty()) { "Navigator must have at least one screen" }

    NavigatorCache {
        val keyHash = currentCompositeKeyHash
        val navigatorCache = LocalNavigatorCache.current
        val currentScreens = navigatorCache[keyHash] ?: initialScreens
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
