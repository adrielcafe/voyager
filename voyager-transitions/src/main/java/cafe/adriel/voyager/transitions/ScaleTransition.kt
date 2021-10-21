package cafe.adriel.voyager.transitions

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.with
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.stack.StackEvent
import cafe.adriel.voyager.navigator.Navigator

private val EnterScales = 1.1f to 0.95f
private val ExitScales = EnterScales.second to EnterScales.first

@ExperimentalAnimationApi
@Composable
public fun ScaleTransition(
    navigator: Navigator,
    modifier: Modifier = Modifier,
    animationSpec: FiniteAnimationSpec<Float> = spring(stiffness = Spring.StiffnessMediumLow),
    onTransitionEnd: () -> Unit = {},
    content: ScreenTransitionContent = { it.Content() }
) {
    ScreenTransition(
        navigator = navigator,
        modifier = modifier,
        content = content,
        onTransitionEnd = onTransitionEnd,
        transition = {
            val (initialScale, targetScale) = when (navigator.lastEvent) {
                StackEvent.Pop -> ExitScales
                else -> EnterScales
            }

            scaleIn(initialScale = initialScale, animationSpec = animationSpec) with
                scaleOut(targetScale = targetScale, animationSpec = animationSpec)
        }
    )
}
