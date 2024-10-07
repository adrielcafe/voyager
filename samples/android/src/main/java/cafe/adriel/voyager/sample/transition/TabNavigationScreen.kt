package cafe.adriel.voyager.sample.transition

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.uniqueScreenKey
import cafe.adriel.voyager.sample.tabNavigation.TabNavigationContent

data object TabNavigationScreen : Screen {
    override val key = uniqueScreenKey

    @Composable
    override fun Content() {
        TabNavigationContent {
            TransitionTab(it.navigator)
        }
    }
}
