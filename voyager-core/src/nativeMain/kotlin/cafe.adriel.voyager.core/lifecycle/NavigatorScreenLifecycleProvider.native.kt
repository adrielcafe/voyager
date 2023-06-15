package cafe.adriel.voyager.core.lifecycle

import cafe.adriel.voyager.core.screen.Screen
internal actual class DefaultNavigatorScreenLifecycleProvider : NavigatorScreenLifecycleProvider {
    actual override fun provide(screen: Screen): List<ScreenLifecycleContentProvider> = emptyList()
}
