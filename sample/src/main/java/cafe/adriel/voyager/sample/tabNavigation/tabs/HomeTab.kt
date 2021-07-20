package cafe.adriel.voyager.sample.tabNavigation.tabs

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import cafe.adriel.voyager.navigator.tab.Tab

object HomeTab : Tab {

    override val title: String
        @Composable get() = "Home"

    override val icon: Painter
        @Composable get() = rememberVectorPainter(Icons.Default.Home)

    @Composable
    override fun Content() {
        TabContent()
    }
}
