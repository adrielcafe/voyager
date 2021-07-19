package cafe.adriel.voyager

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.painter.Painter
import cafe.adriel.voyager.internal.OnBackPressed

public typealias TabNavigatorContent = @Composable (tabNavigator: TabNavigator) -> Unit

public val LocalTabNavigator: ProvidableCompositionLocal<TabNavigator> =
    staticCompositionLocalOf { error("TabNavigator not initialized") }

@Composable
public fun CurrentTab() {
    LocalTabNavigator.current.current.Content()
}

@Composable
public fun TabNavigator(
    defaultTab: Tab,
    onBackPressed: OnBackPressed = { true },
    content: TabNavigatorContent = { CurrentTab() }
) {
    Navigator(defaultTab, onBackPressed) { navigator ->
        val tabNavigator = TabNavigator(navigator)

        CompositionLocalProvider(
            LocalTabNavigator provides tabNavigator
        ) {
            content(tabNavigator)
        }
    }
}

public class TabNavigator internal constructor(
    private val navigator: Navigator
) {

    public var current: Tab
        get() = navigator.last as Tab
        set(tab) = navigator.replaceAll(tab)
}

public interface Tab : Screen {

    public val title: String
        @Composable get

    public val icon: Painter?
        @Composable get() = null
}
