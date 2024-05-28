package cafe.adriel.voyager.core.lifecycle

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import cafe.adriel.voyager.core.annotation.ExperimentalVoyagerApi
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.randomUuid

@Deprecated(
    message = "This API is a wrap on top on DisposableEffect, will be removed in 1.1.0, replace with DisposableEffect"
)
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

@ExperimentalVoyagerApi
@Composable
public fun Screen.LifecycleEffectOnce(onFirstAppear: () -> Unit) {
    val uniqueCompositionKey = rememberSaveable { randomUuid() }

    val lifecycleEffectStore = remember {
        ScreenLifecycleStore.get(this) { LifecycleEffectStore }
    }

    LaunchedEffect(Unit) {
        if (lifecycleEffectStore.hasExecuted(this@LifecycleEffectOnce, uniqueCompositionKey).not()) {
            lifecycleEffectStore.store(this@LifecycleEffectOnce, uniqueCompositionKey)
            onFirstAppear()
        }
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

public interface ScreenLifecycleProvider {

    public fun getLifecycleOwner(): ScreenLifecycleOwner
}
