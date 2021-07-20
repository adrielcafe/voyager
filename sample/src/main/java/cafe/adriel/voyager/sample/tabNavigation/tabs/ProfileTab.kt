package cafe.adriel.voyager.sample.tabNavigation.tabs

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import cafe.adriel.voyager.navigator.tab.Tab

object ProfileTab : Tab {

    override val title: String
        @Composable get() = "Profile"

    override val icon: Painter
        @Composable get() = rememberVectorPainter(Icons.Default.Person)

    @Composable
    override fun Content() {
        TabContent()
    }
}
