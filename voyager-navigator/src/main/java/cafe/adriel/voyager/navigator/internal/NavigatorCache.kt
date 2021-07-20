package cafe.adriel.voyager.navigator.internal

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.staticCompositionLocalOf
import cafe.adriel.voyager.core.screen.Screen

private typealias KeyHash = Int

private typealias NavigatorCache = MutableMap<KeyHash, List<Screen>>

internal val LocalNavigatorCache: ProvidableCompositionLocal<NavigatorCache> =
    staticCompositionLocalOf { error("Navigator cache not initialized") }

@Composable
internal fun NavigatorCache(
    content: @Composable () -> Unit
) {
    val navigatorCache = rememberSaveable<NavigatorCache> { mutableMapOf() }

    CompositionLocalProvider(
        LocalNavigatorCache providesDefault navigatorCache,
        content = content
    )
}
