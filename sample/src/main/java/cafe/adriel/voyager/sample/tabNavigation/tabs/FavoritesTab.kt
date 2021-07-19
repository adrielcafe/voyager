package cafe.adriel.voyager.sample.tabNavigation.tabs

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import cafe.adriel.voyager.Tab

object FavoritesTab : Tab {

    override val title: String
        @Composable get() = "Favorites"

    override val icon: Painter
        @Composable get() = rememberVectorPainter(Icons.Default.Favorite)

    @Composable
    override fun Content() {
        TabContent()
    }
}
