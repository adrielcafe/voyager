package cafe.adriel.voyager.navigator.tab

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import cafe.adriel.voyager.core.screen.Screen

@Composable
public fun CurrentTab() {
    LocalTabNavigator.current.current.Content()
}

public data class TabOptions(
    val index: UShort,
    val title: String,
    val icon: Painter? = null
)

public interface Tab : Screen {

    public val options: TabOptions
        @Composable get

    @Deprecated(
        message = "Use 'options' instead. Will be removed in 1.0.0.",
        replaceWith = ReplaceWith("options")
    )
    public val title: String
        @Composable get() = options.title

    @Deprecated(
        message = "Use 'options' instead. Will be removed in 1.0.0.",
        replaceWith = ReplaceWith("options")
    )
    public val icon: Painter?
        @Composable get() = options.icon
}
