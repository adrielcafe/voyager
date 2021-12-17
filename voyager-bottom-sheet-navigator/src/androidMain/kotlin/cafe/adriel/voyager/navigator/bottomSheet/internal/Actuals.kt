package cafe.adriel.voyager.navigator.bottomSheet.internal

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable

@Composable
internal actual fun BackHandler(enabled: Boolean, onBack: () -> Unit) =
    BackHandler(enabled, onBack)
