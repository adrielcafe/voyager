package cafe.adriel.voyager.sample.multiplatform

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator

@Composable
public fun SampleApplication() {
    Navigator(
        screen = BasicNavigationScreen(index = 0),
        onBackPressed = { currentScreen ->
            println("Navigator: Pop screen #${(currentScreen as BasicNavigationScreen).index}")
            true
        }
    )
}
