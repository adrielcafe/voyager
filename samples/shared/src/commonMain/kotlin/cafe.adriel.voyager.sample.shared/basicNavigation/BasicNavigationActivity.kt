package cafe.adriel.voyager.sample.shared.basicNavigation

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator

@Composable
fun BasicNavigationSample() {
    Navigator(
        screen = BasicNavigationScreen(index = 0),
        onBackPressed = { currentScreen ->
            println("Navigator: Pop screen #${(currentScreen as BasicNavigationScreen).index}")
            true
        }
    )
}
