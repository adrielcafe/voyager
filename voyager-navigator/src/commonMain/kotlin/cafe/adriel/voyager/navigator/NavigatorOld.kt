package cafe.adriel.voyager.navigator

import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.SaveableStateHolder
import cafe.adriel.voyager.core.annotation.InternalVoyagerApi
import cafe.adriel.voyager.core.concurrent.ThreadSafeMap
import cafe.adriel.voyager.core.concurrent.ThreadSafeSet
import cafe.adriel.voyager.core.lifecycle.MultipleProvideBeforeScreenContent
import cafe.adriel.voyager.core.lifecycle.ScreenLifecycleStore
import cafe.adriel.voyager.core.lifecycle.getNavigatorScreenLifecycleProvider
import cafe.adriel.voyager.core.lifecycle.rememberScreenLifecycleOwner
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.stack.SnapshotStateStack
import cafe.adriel.voyager.core.stack.Stack
import cafe.adriel.voyager.navigator.lifecycle.NavigatorKey

public class NavigatorOld @InternalVoyagerApi constructor(
    screens: List<Screen>,
    @InternalVoyagerApi public val key: String,
    private val stateHolder: SaveableStateHolder,
    public val disposeBehavior: NavigatorDisposeBehavior,
    public val parent: NavigatorOld? = null
) : Stack<Screen> by SnapshotStateStack(screens, 0) {

    public val level: Int =
        parent?.level?.inc() ?: 0

    public val lastItem: Screen by derivedStateOf {
        lastItemOrNull ?: error("Navigator has no screen")
    }

    private val stateKeys = ThreadSafeSet<String>()

    internal val children = ThreadSafeMap<NavigatorKey, NavigatorOld>()

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

    private tailrec fun popUntilRoot(navigator: NavigatorOld) {
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
