package cafe.adriel.voyager.navigator.internal

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable

@Composable
internal actual fun BackHandler(enabled: Boolean, onBack: () -> Unit) = BackHandler(enabled, onBack)
