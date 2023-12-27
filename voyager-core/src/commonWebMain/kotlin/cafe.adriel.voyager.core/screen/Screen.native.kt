package cafe.adriel.voyager.core.screen

import androidx.compose.runtime.Composable

public actual interface Screen {
    public actual val key: ScreenKey
        get() = commonKeyGeneration()

    @Composable
    public actual fun Content()
}
