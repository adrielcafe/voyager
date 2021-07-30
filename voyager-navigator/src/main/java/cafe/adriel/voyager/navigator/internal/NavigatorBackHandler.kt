package cafe.adriel.voyager.navigator.internal

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.OnBackPressed
import cafe.adriel.voyager.navigator.currentOrThrow

@Composable
internal fun NavigatorBackHandler(
    onBackPressed: OnBackPressed,
) {
    if (onBackPressed != null) {
        val navigator = LocalNavigator.currentOrThrow

        BackHandler(
            enabled = navigator.canPop || navigator.parent?.canPop ?: false,
            onBack = {
                if (onBackPressed(navigator.lastItem)) {
                    if (navigator.pop().not()) {
                        navigator.parent?.pop()
                    }
                }
            }
        )
    }
}
