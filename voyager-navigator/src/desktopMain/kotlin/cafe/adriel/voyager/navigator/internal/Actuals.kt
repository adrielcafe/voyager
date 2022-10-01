package cafe.adriel.voyager.navigator.internal

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

@Composable
internal actual fun BackHandler(enabled: Boolean, onBack: () -> Unit) = Unit
