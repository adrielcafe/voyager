package cafe.adriel.voyager.core.lifecycle

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import cafe.adriel.voyager.core.annotation.ExperimentalVoyagerApi
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
@ExperimentalVoyagerApi
@InternalVoyagerApi
public fun getNavigatorScreenLifecycleOwner(screen: Screen): List<ScreenLifecycleOwner> {
    val navigatorScreenLifecycleProvider = LocalNavigatorScreenLifecycleProvider.current
    return remember(screen.key) {
        navigatorScreenLifecycleProvider.provide(screen)
    }
}

public interface ScreenLifecycleProvider {

    public fun getLifecycleOwner(): ScreenLifecycleOwner
}
