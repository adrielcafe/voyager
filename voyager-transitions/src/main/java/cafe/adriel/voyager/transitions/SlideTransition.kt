package cafe.adriel.voyager.transitions

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.VisibilityThreshold
import androidx.compose.animation.core.spring
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.with
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntOffset
import cafe.adriel.voyager.core.stack.StackEvent
import cafe.adriel.voyager.navigator.Navigator

@ExperimentalAnimationApi
@Composable
public fun SlideTransition(
    navigator: Navigator,
    modifier: Modifier = Modifier,
    orientation: SlideOrientation = SlideOrientation.Horizontal,
    animationSpec: FiniteAnimationSpec<IntOffset> = spring(
        stiffness = Spring.StiffnessMediumLow,
        visibilityThreshold = IntOffset.VisibilityThreshold
    ),
    onTransitionEnd: () -> Unit = {},
    content: ScreenTransitionContent = { it.Content() }
) {
    ScreenTransition(
        navigator = navigator,
        modifier = modifier,
        content = content,
        onTransitionEnd = onTransitionEnd,
        transition = {
            val (initialOffset, targetOffset) = when (navigator.lastEvent) {
                StackEvent.Pop -> ({ size: Int -> -size }) to ({ size: Int -> size })
                else -> ({ size: Int -> size }) to ({ size: Int -> -size })
            }

            when (orientation) {
                SlideOrientation.Horizontal ->
                    slideInHorizontally(animationSpec, initialOffset) with
                        slideOutHorizontally(animationSpec, targetOffset)
                SlideOrientation.Vertical ->
                    slideInVertically(animationSpec, initialOffset) with
                        slideOutVertically(animationSpec, targetOffset)
            }
        }
    )
}

public enum class SlideOrientation {
    Horizontal,
    Vertical
}
