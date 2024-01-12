package cafe.adriel.voyager.navigator.internal

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.OnBackPressed

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
                with(navigator) {
                    if (onBackPressed(lastItem)) {
                        if (pop(lastItem).not()) {
                            parent?.pop(lastItem)
                        }
                    }
                }
            }
        )
    }
}
