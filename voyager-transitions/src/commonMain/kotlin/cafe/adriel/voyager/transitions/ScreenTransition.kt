package cafe.adriel.voyager.transitions

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.annotation.ExperimentalVoyagerApi
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.stack.StackEvent
import cafe.adriel.voyager.navigator.Navigator

@ExperimentalVoyagerApi
public interface ScreenTransition {

    /**
     * Defines the enter transition for the Screen.
     *
     * Returns null when it should not define a transition for this screen.
     */
    public fun enter(): EnterTransition? = null

    /**
     * Defines the exit transition for the Screen.
     *
     * Returns null when it should not define a transition for this screen.
     */
    public fun exit(): ExitTransition? = null
}

public typealias ScreenTransitionContent = @Composable AnimatedVisibilityScope.(Screen) -> Unit

@Composable
public fun ScreenTransition(
    navigator: Navigator,
    enterTransition: AnimatedContentTransitionScope<Screen>.() -> ContentTransform,
    exitTransition: AnimatedContentTransitionScope<Screen>.() -> ContentTransform,
    modifier: Modifier = Modifier,
    content: ScreenTransitionContent = { it.Content() }
) {
    ScreenTransition(
        navigator = navigator,
        modifier = modifier,
        content = content,
        transition = {
            when (navigator.lastEvent) {
                StackEvent.Pop -> exitTransition()
                else -> enterTransition()
            }
        }
    )
}

@Composable
public fun ScreenTransition(
    navigator: Navigator,
    transition: AnimatedContentTransitionScope<Screen>.() -> ContentTransform,
    modifier: Modifier = Modifier,
    content: ScreenTransitionContent = { it.Content() }
) {
    AnimatedContent(
        targetState = navigator.lastItem,
        transitionSpec = {
            val contentTransform = transition()

            val isPop = navigator.lastEvent == StackEvent.Pop

            val screenEnterTransition = if (isPop) {
                (targetState as? ScreenTransition)?.enter()
            } else {
                (targetState as? ScreenTransition)?.enter()
            }

            val screenExitTransition = if (isPop) {
                (initialState as? ScreenTransition)?.exit()
            } else {
                (initialState as? ScreenTransition)?.exit()
            }

            if (screenExitTransition != null || screenEnterTransition != null) {
                (screenEnterTransition ?: contentTransform.targetContentEnter) togetherWith
                    (screenExitTransition ?: contentTransform.initialContentExit)
            } else {
                contentTransform
            }
        },
        modifier = modifier
    ) { screen ->
        navigator.saveableState("transition", screen) {
            content(screen)
        }
    }
}
