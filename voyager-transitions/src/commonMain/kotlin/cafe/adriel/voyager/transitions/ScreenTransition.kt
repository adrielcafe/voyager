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
     * @param lastEvent - lastEvent in the navigation stack.
     * @return EnterTransition or null when it should not define a transition for this screen.
     */
    public fun enter(lastEvent: StackEvent): EnterTransition? = null

    /**
     * Defines the exit transition for the Screen.
     *
     * @param lastEvent - lastEvent in the navigation stack.
     * @return ExitTransition or null when it should not define a transition for this screen.
     */
    public fun exit(lastEvent: StackEvent): ExitTransition? = null
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

@ExperimentalVoyagerApi
@Composable
public fun ScreenTransition(
    navigator: Navigator,
    defaultTransition: ScreenTransition,
    modifier: Modifier = Modifier,
    content: ScreenTransitionContent = { it.Content() }
) {
    ScreenTransition(
        navigator = navigator,
        transition = {
            val enter = defaultTransition.enter(navigator.lastEvent) ?: EnterTransition.None
            val exit = defaultTransition.exit(navigator.lastEvent) ?: ExitTransition.None
            enter togetherWith exit
        },
        modifier = modifier,
        content = content
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

            val sourceScreenTransition = when (navigator.lastEvent) {
                StackEvent.Pop, StackEvent.Replace -> initialState
                else -> targetState
            } as? ScreenTransition

            val screenEnterTransition = sourceScreenTransition?.enter(navigator.lastEvent)
                ?: contentTransform.targetContentEnter

            val screenExitTransition = sourceScreenTransition?.exit(navigator.lastEvent)
                ?: contentTransform.initialContentExit

            screenEnterTransition togetherWith screenExitTransition
        },
        modifier = modifier
    ) { screen ->
        navigator.saveableState("transition", screen) {
            content(screen)
        }
    }
}
