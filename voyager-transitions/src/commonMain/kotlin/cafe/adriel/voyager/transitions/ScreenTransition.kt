package cafe.adriel.voyager.transitions

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
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

@ExperimentalVoyagerApi
@Composable
public fun ScreenTransition(
    navigator: Navigator,
    enterTransition: AnimatedContentTransitionScope<Screen>.() -> ContentTransform,
    exitTransition: AnimatedContentTransitionScope<Screen>.() -> ContentTransform,
    modifier: Modifier = Modifier,
    disposeScreenAfterTransitionEnd: Boolean = false,
    content: ScreenTransitionContent = { it.Content() }
) {
    ScreenTransition(
        navigator = navigator,
        modifier = modifier,
        disposeScreenAfterTransitionEnd = disposeScreenAfterTransitionEnd,
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
    enterTransition: AnimatedContentTransitionScope<Screen>.() -> ContentTransform,
    exitTransition: AnimatedContentTransitionScope<Screen>.() -> ContentTransform,
    modifier: Modifier = Modifier,
    content: ScreenTransitionContent = { it.Content() }
) {
    ScreenTransition(
        navigator = navigator,
        enterTransition = enterTransition,
        exitTransition = exitTransition,
        modifier = modifier,
        content = content
    )
}

@ExperimentalVoyagerApi
@Composable
public fun ScreenTransition(
    navigator: Navigator,
    defaultTransition: ScreenTransition,
    modifier: Modifier = Modifier,
    disposeScreenAfterTransitionEnd: Boolean = false,
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
        disposeScreenAfterTransitionEnd = disposeScreenAfterTransitionEnd,
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
    ScreenTransition(
        navigator = navigator,
        transition = transition,
        modifier = modifier,
        disposeScreenAfterTransitionEnd = false,
        content = content
    )
}

@ExperimentalVoyagerApi
@OptIn(ExperimentalAnimationApi::class)
@Composable
public fun ScreenTransition(
    navigator: Navigator,
    transition: AnimatedContentTransitionScope<Screen>.() -> ContentTransform,
    modifier: Modifier = Modifier,
    disposeScreenAfterTransitionEnd: Boolean = false,
    content: ScreenTransitionContent = { it.Content() }
) {
    val screenCandidatesToDispose = rememberSaveable(saver = screenCandidatesToDisposeSaver()) {
        mutableStateOf(emptySet())
    }

    val currentScreens = navigator.items

    if (disposeScreenAfterTransitionEnd) {
        DisposableEffect(currentScreens) {
            onDispose {
                val newScreenKeys = navigator.items.map { it.key }
                screenCandidatesToDispose.value += currentScreens.filter { it.key !in newScreenKeys }
            }
        }
    }

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

            ContentTransform(
                screenEnterTransition,
                screenExitTransition,
                contentTransform.targetContentZIndex,
                contentTransform.sizeTransform,
            )
        },
        modifier = modifier
    ) { screen ->
        if (this.transition.targetState == this.transition.currentState && disposeScreenAfterTransitionEnd) {
            LaunchedEffect(Unit) {
                val newScreens = navigator.items.map { it.key }
                val screensToDispose = screenCandidatesToDispose.value.filterNot { it.key in newScreens }
                if (screensToDispose.isNotEmpty()) {
                    screensToDispose.forEach { navigator.dispose(it) }
                    navigator.clearEvent()
                }
                screenCandidatesToDispose.value = emptySet()
            }
        }

        navigator.saveableState("transition", screen) {
            content(screen)
        }
    }
}

private fun screenCandidatesToDisposeSaver(): Saver<MutableState<Set<Screen>>, List<Screen>> {
    return Saver(
        save = { it.value.toList() },
        restore = { mutableStateOf(it.toSet()) }
    )
}
