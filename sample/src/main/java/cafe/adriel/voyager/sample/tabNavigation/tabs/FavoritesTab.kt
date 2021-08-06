package cafe.adriel.voyager.sample.tabNavigation.tabs

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions

object FavoritesTab : Tab {

    override val options: TabOptions
        @Composable
        get() {
            val icon = rememberVectorPainter(Icons.Default.Favorite)

            return remember {
                TabOptions(
                    index = 1u,
                    title = "Favorites",
                    icon = icon
                )
            }
        }

    @Composable
    override fun Content() {
        TabContent()
    }
}
