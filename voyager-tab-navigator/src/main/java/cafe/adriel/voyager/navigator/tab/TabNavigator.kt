package cafe.adriel.voyager.navigator.tab

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.SaveableStateHolder
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import cafe.adriel.voyager.navigator.tab.internal.rememberTabNavigator

public typealias TabNavigatorContent = @Composable (tabNavigator: TabNavigator) -> Unit

public val LocalTabNavigator: ProvidableCompositionLocal<TabNavigator> =
    staticCompositionLocalOf { error("TabNavigator not initialized") }

@Composable
public fun TabNavigator(
    tab: Tab,
    content: TabNavigatorContent = { CurrentTab() }
) {
    val tabNavigator = rememberTabNavigator(tab)

    CompositionLocalProvider(
        LocalTabNavigator provides tabNavigator
    ) {
        content(tabNavigator)
    }
}

public class TabNavigator internal constructor(
    tab: Tab,
    public val stateHolder: SaveableStateHolder
) {

    public var current: Tab by mutableStateOf(tab)
}
