package cafe.adriel.voyager.navigator.tab

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import cafe.adriel.voyager.navigator.Navigator

public typealias TabNavigatorContent = @Composable (tabNavigator: TabNavigator) -> Unit

public val LocalTabNavigator: ProvidableCompositionLocal<TabNavigator> =
    staticCompositionLocalOf { error("TabNavigator not initialized") }

@Composable
public fun TabNavigator(
    tab: Tab,
    content: TabNavigatorContent = { CurrentTab() }
) {
    Navigator(tab, autoDispose = false, onBackPressed = null) { navigator ->
        val tabNavigator = remember(navigator) {
            TabNavigator(navigator)
        }

        CompositionLocalProvider(LocalTabNavigator provides tabNavigator) {
            content(tabNavigator)
        }
    }
}

public class TabNavigator internal constructor(
    private val navigator: Navigator
) {

    public var current: Tab
        get() = navigator.lastItem as Tab
        set(tab) = navigator.replaceAll(tab)
}
