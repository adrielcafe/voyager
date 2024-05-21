package cafe.adriel.voyager.navigator.internal

import cafe.adriel.voyager.core.lifecycle.NavigatorScreenLifecycleProvider
import cafe.adriel.voyager.core.lifecycle.ScreenLifecycleContentProvider
import cafe.adriel.voyager.core.screen.Screen

internal actual class DefaultNavigatorScreenLifecycleProvider actual constructor() : NavigatorScreenLifecycleProvider {
    actual override fun provide(screen: Screen): List<ScreenLifecycleContentProvider> =
        emptyList()
}
