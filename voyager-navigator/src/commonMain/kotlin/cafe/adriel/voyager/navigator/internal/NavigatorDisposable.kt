package cafe.adriel.voyager.navigator.internal

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import cafe.adriel.voyager.core.lifecycle.ScreenLifecycleStore
import cafe.adriel.voyager.core.model.ScreenModelStore
import cafe.adriel.voyager.core.stack.StackEvent
import cafe.adriel.voyager.navigator.Navigator

private val disposableEvents: Set<StackEvent> =
    setOf(StackEvent.Pop, StackEvent.Replace)

@Composable
internal fun NavigatorDisposableEffect(
    navigator: Navigator
) {
    DisposableEffect(navigator) {
        onDispose {
            for (screen in navigator.items) {
                ScreenModelStore.remove(screen)
                ScreenLifecycleStore.remove(screen)
                navigator.stateHolder.removeState(screen.key)
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
                ScreenModelStore.remove(currentScreen)
                ScreenLifecycleStore.remove(currentScreen)
                navigator.stateHolder.removeState(currentScreen.key)
                navigator.clearEvent()
            }
        }
    }
}
