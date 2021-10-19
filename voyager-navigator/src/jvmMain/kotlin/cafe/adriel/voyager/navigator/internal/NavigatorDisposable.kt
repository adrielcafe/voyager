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
    navigator: Navigator,
    onDispose: () -> Unit
) {
    val currentScreen = navigator.lastItem

    DisposableEffect(currentScreen.key) {
        onDispose {
            val disposePreviousScreen = navigator.lastEvent in disposableEvents
            val disposeInitialScreen = navigator.lastEvent == StackEvent.Idle && navigator.canPop.not()
            if (disposePreviousScreen || disposeInitialScreen) {
                onDispose()
                ScreenModelStore.remove(currentScreen)
                ScreenLifecycleStore.remove(currentScreen)
                navigator.stateHolder.removeState(currentScreen.key)
                navigator.clearEvent()
            }
        }
    }
}
