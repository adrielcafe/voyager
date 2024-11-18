package cafe.adriel.voyager.navigator.tab

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import cafe.adriel.voyager.core.screen.Screen

@Composable
public fun CurrentTab() {
    val tabNavigator = LocalTabNavigator.current
    val currentTab = tabNavigator.current

    tabNavigator.saveableState("currentTab") {
        currentTab.Content()
    }
}

public interface TabOptions {
    public val index: UShort
    public val title: String
    public val icon: Painter?
}

public interface Tab : Screen {

    public val options: TabOptions
        @Composable get
}
