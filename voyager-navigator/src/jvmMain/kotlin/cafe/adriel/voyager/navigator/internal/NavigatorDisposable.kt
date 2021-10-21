package cafe.adriel.voyager.navigator.internal

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import cafe.adriel.voyager.core.stack.StackEvent
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.TransitionCallback

private val disposableEvents: Set<StackEvent> =
    setOf(StackEvent.Pop, StackEvent.Replace)

@Composable
internal fun NavigatorDisposableEffect(
    navigator: Navigator,
    transitionCallback: TransitionCallback
) {
    val currentScreen = navigator.lastItem

    DisposableEffect(currentScreen.key) {
        onDispose {
            // onDispose is async and we need check again for transitions
            if (navigator.hasTransitionCallback.not()) {
                val disposePreviousScreen = navigator.lastEvent in disposableEvents
                val disposeInitialScreen = navigator.lastEvent == StackEvent.Idle && navigator.canPop.not()
                if (disposePreviousScreen || disposeInitialScreen) {
                    transitionCallback.onTransitionEnd(navigator, currentScreen)
                }
            }
        }
    }
}
