package cafe.adriel.voyager.navigator.bottomSheet.internal

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.bottomSheet.BottomSheetNavigator

@Composable
internal expect fun BackHandler(enabled: Boolean, onBack: () -> Unit)

@ExperimentalMaterial3Api
@Composable
internal fun BottomSheetNavigatorBackHandler(
    navigator: BottomSheetNavigator,
    sheetState: SheetState,
    hideOnBackPress: Boolean
) {
    BackHandler(enabled = sheetState.isVisible) {
        if (navigator.pop().not() && hideOnBackPress) {
            navigator.hide()
        }
    }
}
