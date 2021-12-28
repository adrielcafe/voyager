package cafe.adriel.voyager.core.screen

import androidx.compose.runtime.Composable

public interface Screen {

    public val key: ScreenKey
        get() = this::class.qualifiedName ?: error("Default ScreenKey not found, please provide your own key")

    @Composable
    public fun Content()
}
