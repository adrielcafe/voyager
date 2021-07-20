package cafe.adriel.voyager.navigator.tab

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import cafe.adriel.voyager.core.screen.Screen

@Composable
public fun CurrentTab() {
    LocalTabNavigator.current.current.Content()
}

public interface Tab : Screen {

    public val title: String
        @Composable get

    public val icon: Painter?
        @Composable get() = null
}
