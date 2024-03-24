package cafe.adriel.voyager.navigator.internal

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.annotation.InternalVoyagerApi

@InternalVoyagerApi
@Composable
public actual fun BackHandler(enabled: Boolean, onBack: () -> Unit): Unit = Unit
