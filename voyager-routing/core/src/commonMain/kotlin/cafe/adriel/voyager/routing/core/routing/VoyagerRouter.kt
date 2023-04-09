package cafe.adriel.voyager.routing.core.routing

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import cafe.adriel.voyager.navigator.CurrentScreen
import cafe.adriel.voyager.navigator.Navigator

@Composable
public fun VoyagerRouter(router: VoyagerRouting) {
    DisposableEffect(router) {
        onDispose {
            router.dispose()
        }
    }

    CompositionLocalProvider(VoyagerLocalRouter provides router) {
        Navigator(
            screen = router.initialScreen(uri = null) // TODO: Get initial uri from a deep link
        ) { navigator ->
            val event by router.navigation.collectAsState()

            LaunchedEffect(key1 = event) {
                event.execute(navigator)
                if (event !is VoyagerRouteEvent.Idle) {
                    router.clearEvent()
                }
            }

            CurrentScreen()
        }
    }
}
