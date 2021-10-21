package cafe.adriel.voyager.transitions

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.updateTransition
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.stack.StackEvent
import cafe.adriel.voyager.navigator.Navigator

@OptIn(ExperimentalAnimationApi::class)
public typealias ScreenTransitionContent = @Composable AnimatedVisibilityScope.(Screen) -> Unit

@ExperimentalAnimationApi
@Composable
public fun ScreenTransition(
    navigator: Navigator,
    enterTransition: AnimatedContentScope<Screen>.() -> ContentTransform,
    exitTransition: AnimatedContentScope<Screen>.() -> ContentTransform,
    modifier: Modifier = Modifier,
    onTransitionEnd: () -> Unit = {},
    content: ScreenTransitionContent = { it.Content() }
) {
    ScreenTransition(
        navigator = navigator,
        modifier = modifier,
        content = content,
        onTransitionEnd = onTransitionEnd,
        transition = {
            when (navigator.lastEvent) {
                StackEvent.Pop -> exitTransition()
                else -> enterTransition()
            }
        }
    )
}

@ExperimentalAnimationApi
@Composable
public fun ScreenTransition(
    navigator: Navigator,
    transition: AnimatedContentScope<Screen>.() -> ContentTransform,
    modifier: Modifier = Modifier,
    onTransitionEnd: () -> Unit = {},
    content: ScreenTransitionContent = { it.Content() }
) {
    val updateTransition = updateTransition(targetState = navigator.lastItem, label = "ScreenTransition")
    val currentScreen = updateTransition.currentState

    DisposableEffect(currentScreen) {
        navigator.registerToDisposeAfterTransitionEnd(currentScreen)
        onDispose {
            navigator.notifyTransitionEnd(previous = currentScreen)
            onTransitionEnd.invoke()
        }
    }

    updateTransition.AnimatedContent(
        transitionSpec = transition,
        modifier = modifier
    ) { screen ->
        navigator.stateHolder.SaveableStateProvider(screen.key) {
            content(screen)
        }
    }
}
