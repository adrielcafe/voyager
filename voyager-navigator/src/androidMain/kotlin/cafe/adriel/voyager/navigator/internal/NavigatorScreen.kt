package cafe.adriel.voyager.navigator.internal

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.annotation.InternalVoyagerApi
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey

@PublishedApi
@InternalVoyagerApi
internal data class NavigatorScreen(
    override val key: ScreenKey
) : Screen {
    @Composable
    override fun Content() {
        error("NavigatorScreen content must never be called")
    }
}
