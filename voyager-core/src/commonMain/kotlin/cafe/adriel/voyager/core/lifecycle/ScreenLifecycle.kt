package cafe.adriel.voyager.core.lifecycle

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import cafe.adriel.voyager.core.annotation.InternalVoyagerApi
import cafe.adriel.voyager.core.screen.Screen

@Composable
public fun Screen.LifecycleEffect(
    onStarted: () -> Unit = {},
    onDisposed: () -> Unit = {}
) {
    DisposableEffect(key) {
        onStarted()
        onDispose(onDisposed)
    }
}

@Composable
public fun rememberScreenLifecycleOwner(
    screen: Screen
): ScreenLifecycleOwner =
    remember(screen.key) {
        when (screen) {
            is ScreenLifecycleProvider -> screen.getLifecycleOwner()
            else -> DefaultScreenLifecycleOwner
        }
    }

@Composable
@InternalVoyagerApi
public fun getNavigatorScreenLifecycleProvider(screen: Screen): List<ScreenLifecycleContentProvider> {
    val navigatorScreenLifecycleProvider = LocalNavigatorScreenLifecycleProvider.current
    return remember(screen.key) {
        navigatorScreenLifecycleProvider.provide(screen)
    }
}

public interface ScreenLifecycleProvider {

    public fun getLifecycleOwner(): ScreenLifecycleOwner
}
