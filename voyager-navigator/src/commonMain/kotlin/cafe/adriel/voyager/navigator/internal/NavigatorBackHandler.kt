package cafe.adriel.voyager.navigator.internal

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.OnBackPressed

// FIXME: Composable function expect with default parameter seems to be breaking on code generation
@Composable
internal expect fun BackHandler(enabled: Boolean, onBack: () -> Unit)

@Composable
internal fun NavigatorBackHandler(
    navigator: Navigator,
    onBackPressed: OnBackPressed
) {
    if (onBackPressed != null) {
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
