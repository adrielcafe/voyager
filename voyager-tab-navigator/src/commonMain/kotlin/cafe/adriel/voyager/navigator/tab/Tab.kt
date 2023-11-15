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

public data class TabOptions(
    val index: UShort,
    val title: String,
    val icon: Painter? = null
)

public interface Tab : Screen {

    public val options: TabOptions
        @Composable get
}
