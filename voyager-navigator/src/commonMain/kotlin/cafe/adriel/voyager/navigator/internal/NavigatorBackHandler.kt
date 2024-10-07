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
    if (onBackPressed == null) {
        BackHandler(
            enabled = navigator.size > 1,
            onBack = navigator::popRecursively
        )
    } else {
        // `navigator.size == 1` covers onBackPressed = { false } and empty stack
        // because `navigator.canPop` always returns `false` when stack min size is 1
        BackHandler(
            enabled = (navigator.size == 1 && !onBackPressed(navigator.lastItem)) ||
                navigator.canPop ||
                navigator.parent?.canPop ?: false,
            onBack = {
                if (onBackPressed(navigator.lastItem)) {
                    navigator.popRecursively()
                }
            }
        )
    }
}
