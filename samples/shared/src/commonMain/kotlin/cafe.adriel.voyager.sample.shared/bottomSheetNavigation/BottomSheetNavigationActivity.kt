package cafe.adriel.voyager.sample.shared.bottomSheetNavigation

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.bottomSheet.BottomSheetNavigator

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BottomSheetNavigationSample() {
    BottomSheetNavigator {
        Navigator(BackScreen())
    }
}
