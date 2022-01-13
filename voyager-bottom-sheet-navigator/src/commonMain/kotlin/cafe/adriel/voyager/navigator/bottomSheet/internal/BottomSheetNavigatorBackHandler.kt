package cafe.adriel.voyager.navigator.bottomSheet.internal

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.bottomSheet.BottomSheetNavigator

@Composable
internal expect fun BackHandler(enabled: Boolean = true, onBack: () -> Unit)

@ExperimentalMaterialApi
@Composable
internal fun BottomSheetNavigatorBackHandler(
    navigator: BottomSheetNavigator,
    sheetState: ModalBottomSheetState,
    hideOnBackPress: Boolean
) {
    if (sheetState.isVisible) {
        BackHandler {
            if (navigator.pop().not() && hideOnBackPress) {
                navigator.hide()
            }
        }
    }
}
