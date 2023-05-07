package cafe.adriel.voyager.core.screen

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.platform.multiplatformName

public expect interface Screen {

    public open val key: ScreenKey

    @Composable
    public fun Content()
}

internal fun Screen.commonKeyGeneration() =
    this::class.multiplatformName ?: error("Default ScreenKey not found, please provide your own key")
