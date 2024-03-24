package cafe.adriel.voyager.navigator.internal

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.annotation.InternalVoyagerApi
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.OnBackPressed

@InternalVoyagerApi
@Composable
public expect fun BackHandler(enabled: Boolean, onBack: () -> Unit)

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
