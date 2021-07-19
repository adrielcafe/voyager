package cafe.adriel.voyager.internal

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.LocalNavigator
import cafe.adriel.voyager.Screen
import cafe.adriel.voyager.currentOrThrow

internal typealias OnBackPressed = ((currentScreen: Screen) -> Boolean)?

@Composable
internal fun NavigatorBackHandler(
    onBackPressed: OnBackPressed,
) {
    if (onBackPressed != null) {
        val navigator = LocalNavigator.currentOrThrow

        BackHandler(
            enabled = navigator.canPop || navigator.parent?.canPop ?: false,
            onBack = {
                if (onBackPressed(navigator.last)) {
                    if (navigator.pop().not()) {
                        navigator.parent?.pop()
                    }
                }
            }
        )
    }
}
