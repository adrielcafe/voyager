package cafe.adriel.voyager.transitions

import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.Transition
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.stack.StackEvent
import cafe.adriel.voyager.navigator.Navigator

public typealias ScreenTransitionContent = @Composable (Screen) -> Unit

public typealias ScreenTransitionModifier = @Composable (Screen, Transition<Screen>, StackEvent) -> Modifier

@Composable
public fun ScreenTransition(
    navigator: Navigator,
    transitionModifier: ScreenTransitionModifier,
    modifier: Modifier = Modifier,
    content: ScreenTransitionContent = { it.Content() }
) {
    val currentScreen = navigator.lastItem
    val currentEvent = navigator.lastEvent

    val items = remember { mutableStateListOf<ScreenTransitionItem>() }
    val transitionState = remember { MutableTransitionState(currentScreen) }
    val targetChanged = (currentScreen != transitionState.targetState)
    transitionState.targetState = currentScreen
    val transition = updateTransition(
        transitionState = transitionState,
        label = "ScreenTransition"
    )

    if (targetChanged || items.isEmpty()) {
        val keys = items
            .map { it.screen }
            .run {
                if (contains(currentScreen)) this
                else toMutableList().also { it.add(currentScreen) }
            }
        items.clear()
        keys.mapTo(items) { key ->
            ScreenTransitionItem(key) {
                Box(transitionModifier(key, transition, currentEvent)) {
                    content(key)
                }
            }
        }
    } else if (transitionState.currentState == transitionState.targetState) {
        items.removeAll { it.screen != transitionState.targetState }
    }

    Box(modifier) {
        items.forEach {
            key(it.screen.key) {
                navigator.stateHolder.SaveableStateProvider(it.screen.key) {
                    it.content()
                }
            }
        }
    }
}

private data class ScreenTransitionItem(
    val screen: Screen,
    val content: @Composable () -> Unit
)
