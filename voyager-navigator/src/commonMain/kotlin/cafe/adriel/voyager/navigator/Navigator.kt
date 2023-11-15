package cafe.adriel.voyager.navigator

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.currentCompositeKeyHash
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.SaveableStateHolder
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.runtime.staticCompositionLocalOf
import cafe.adriel.voyager.core.annotation.InternalVoyagerApi
import cafe.adriel.voyager.core.concurrent.ThreadSafeMap
import cafe.adriel.voyager.core.concurrent.ThreadSafeSet
import cafe.adriel.voyager.core.lifecycle.MultipleProvideBeforeScreenContent
import cafe.adriel.voyager.core.lifecycle.ScreenLifecycleStore
import cafe.adriel.voyager.core.lifecycle.getNavigatorScreenLifecycleProvider
import cafe.adriel.voyager.core.lifecycle.rememberScreenLifecycleOwner
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.stack.Stack
import cafe.adriel.voyager.core.stack.toMutableStateStack
import cafe.adriel.voyager.navigator.internal.ChildrenNavigationDisposableEffect
import cafe.adriel.voyager.navigator.internal.LocalNavigatorStateHolder
import cafe.adriel.voyager.navigator.internal.NavigatorBackHandler
import cafe.adriel.voyager.navigator.internal.NavigatorDisposableEffect
import cafe.adriel.voyager.navigator.internal.StepDisposableEffect
import cafe.adriel.voyager.navigator.internal.rememberNavigator
import cafe.adriel.voyager.navigator.lifecycle.NavigatorKey

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
    key: String = compositionUniqueId(),
    content: NavigatorContent = { CurrentScreen() }
) {
    Navigator(
        screens = listOf(screen),
        disposeBehavior = disposeBehavior,
        onBackPressed = onBackPressed,
        key = key,
        content = content
    )
}

@Composable
public fun Navigator(
    screens: List<Screen>,
    disposeBehavior: NavigatorDisposeBehavior = NavigatorDisposeBehavior(),
    onBackPressed: OnBackPressed = { true },
    key: String = compositionUniqueId(),
    content: NavigatorContent = { CurrentScreen() }
) {
    require(screens.isNotEmpty()) { "Navigator must have at least one screen" }
    require(key.isNotEmpty()) { "Navigator key can't be empty" }

    CompositionLocalProvider(
        LocalNavigatorStateHolder providesDefault rememberSaveableStateHolder()
    ) {
        val navigator = rememberNavigator(screens, key, disposeBehavior, LocalNavigator.current)

        if (navigator.parent?.disposeBehavior?.disposeNestedNavigators != false) {
            NavigatorDisposableEffect(navigator)
        }

        CompositionLocalProvider(
            LocalNavigator provides navigator
        ) {
            if (disposeBehavior.disposeSteps) {
                StepDisposableEffect(navigator)
            }

            NavigatorBackHandler(navigator, onBackPressed)

            content(navigator)
        }

        ChildrenNavigationDisposableEffect(navigator)
    }
}

public class Navigator @InternalVoyagerApi constructor(
    screens: List<Screen>,
    @InternalVoyagerApi public val key: String,
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

    internal val children = ThreadSafeMap<NavigatorKey, Navigator>()

    @Composable
    public fun saveableState(
        key: String,
        screen: Screen = lastItem,
        content: @Composable () -> Unit
    ) {
        val stateKey = "${screen.key}:$key"
        stateKeys += stateKey

        @Composable
        fun provideSaveableState(suffixKey: String, content: @Composable () -> Unit) {
            val providedStateKey = "$stateKey:$suffixKey"
            stateKeys += providedStateKey
            stateHolder.SaveableStateProvider(providedStateKey, content)
        }

        val lifecycleOwner = rememberScreenLifecycleOwner(screen)
        val navigatorScreenLifecycleOwners = getNavigatorScreenLifecycleProvider(screen)

        val composed = remember(lifecycleOwner, navigatorScreenLifecycleOwners) {
            listOf(lifecycleOwner) + navigatorScreenLifecycleOwners
        }
        MultipleProvideBeforeScreenContent(
            screenLifecycleContentProviders = composed,
            provideSaveableState = { suffix, content -> provideSaveableState(suffix, content) },
            content = {
                stateHolder.SaveableStateProvider(stateKey, content)
            }
        )
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

    @InternalVoyagerApi
    public fun dispose(
        screen: Screen
    ) {
        ScreenLifecycleStore.remove(screen)
        stateKeys
            .toSet() // Copy
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
    val disposeSteps: Boolean = true
)

@InternalVoyagerApi
@Composable
public fun compositionUniqueId(): String = currentCompositeKeyHash.toString(MaxSupportedRadix)

private val MaxSupportedRadix = 36
