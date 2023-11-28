package cafe.adriel.voyager.navigator.tab

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import cafe.adriel.voyager.core.lifecycle.DisposableEffectIgnoringConfiguration
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.NavigatorDisposeBehavior
import cafe.adriel.voyager.navigator.compositionUniqueId

public typealias TabNavigatorContent = @Composable (tabNavigator: TabNavigator) -> Unit

public val LocalTabNavigator: ProvidableCompositionLocal<TabNavigator> =
    staticCompositionLocalOf { error("TabNavigator not initialized") }

@Composable
public fun TabNavigator(
    tab: Tab,
    disposeNestedNavigators: Boolean = false,
    tabDisposable: (@Composable (TabNavigator) -> Unit)? = null,
    key: String = compositionUniqueId(),
    content: TabNavigatorContent = { CurrentTab() }
) {
    Navigator(
        screen = tab,
        disposeBehavior = NavigatorDisposeBehavior(
            disposeNestedNavigators = disposeNestedNavigators,
            disposeSteps = false
        ),
        onBackPressed = null,
        key = key
    ) { navigator ->
        val tabNavigator = remember(navigator) {
            TabNavigator(navigator)
        }

        tabDisposable?.invoke(tabNavigator)

        CompositionLocalProvider(LocalTabNavigator provides tabNavigator) {
            content(tabNavigator)
        }
    }
}

@Composable
public fun TabDisposable(navigator: TabNavigator, tabs: List<Tab>) {
    DisposableEffectIgnoringConfiguration(Unit) {
        onDispose {
            tabs.forEach {
                navigator.navigator.dispose(it)
            }
        }
    }
}

public class TabNavigator internal constructor(
    internal val navigator: Navigator
) {

    public var current: Tab
        get() = navigator.lastItem as Tab
        set(tab) = navigator.replaceAll(tab)

    @Composable
    public fun saveableState(
        key: String,
        tab: Tab = current,
        content: @Composable () -> Unit
    ) {
        navigator.saveableState(key, tab, content = content)
    }
}
