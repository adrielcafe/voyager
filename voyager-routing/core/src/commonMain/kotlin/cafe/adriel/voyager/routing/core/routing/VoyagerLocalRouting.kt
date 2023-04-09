package cafe.adriel.voyager.routing.core.routing

import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf

public val LocalRouter: ProvidableCompositionLocal<VoyagerRouting?> =
    staticCompositionLocalOf { null }
