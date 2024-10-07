package cafe.adriel.voyager.navigator.internal

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import cafe.adriel.voyager.core.lifecycle.LocalNavigatorScreenLifecycleProvider
import cafe.adriel.voyager.core.lifecycle.NavigatorScreenLifecycleProvider
import cafe.adriel.voyager.core.lifecycle.ScreenLifecycleContentProvider
import cafe.adriel.voyager.core.screen.Screen

internal val defaultNavigatorScreenLifecycleProvider = DefaultNavigatorScreenLifecycleProvider()

internal expect class DefaultNavigatorScreenLifecycleProvider() : NavigatorScreenLifecycleProvider {
    override fun provide(screen: Screen): List<ScreenLifecycleContentProvider>
}

@Composable
internal fun getNavigatorScreenLifecycleProvider(screen: Screen): List<ScreenLifecycleContentProvider> {
    val navigatorScreenLifecycleProvider = LocalNavigatorScreenLifecycleProvider.current
        ?: remember { defaultNavigatorScreenLifecycleProvider }

    return remember(screen.key) {
        navigatorScreenLifecycleProvider.provide(screen)
    }
}
