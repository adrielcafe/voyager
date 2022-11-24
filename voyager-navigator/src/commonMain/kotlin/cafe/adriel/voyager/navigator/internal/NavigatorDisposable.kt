package cafe.adriel.voyager.navigator.internal

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import cafe.adriel.voyager.core.lifecycle.DisposableEffectIgnoringConfiguration
import cafe.adriel.voyager.core.lifecycle.ScreenLifecycleOwner
import cafe.adriel.voyager.core.stack.StackEvent
import cafe.adriel.voyager.navigator.Navigator

private val disposableEvents: Set<StackEvent> =
    setOf(StackEvent.Pop, StackEvent.Replace)

@Composable
internal fun NavigatorDisposableEffect(
    navigator: Navigator
) {
    DisposableEffectIgnoringConfiguration(navigator) {
        onDispose {
            for (screen in navigator.items) {
                navigator.dispose(screen)
            }
            navigator.clearEvent()
        }
    }
}

@Composable
internal fun StepDisposableEffect(
    navigator: Navigator
) {
    val currentScreen = navigator.lastItem

    DisposableEffect(currentScreen.key) {
        onDispose {
            if (navigator.lastEvent in disposableEvents) {
                navigator.dispose(currentScreen)
                navigator.clearEvent()
            }
        }
    }
}

@Composable
internal fun LifecycleDisposableEffect(
    lifecycleOwner: ScreenLifecycleOwner
) {
    DisposableEffect(lifecycleOwner) {
        lifecycleOwner.onStart()
        onDispose(lifecycleOwner::onStop)
    }
}
