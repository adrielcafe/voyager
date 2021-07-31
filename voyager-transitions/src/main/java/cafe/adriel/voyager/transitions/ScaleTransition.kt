package cafe.adriel.voyager.transitions

import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import cafe.adriel.voyager.core.stack.StackEvent
import cafe.adriel.voyager.navigator.Navigator

@Composable
public fun ScaleTransition(
    navigator: Navigator,
    modifier: Modifier = Modifier,
    animationSpec: FiniteAnimationSpec<Float> = tween(),
    content: ScreenTransitionContent = { it.Content() }
) {
    ScreenTransition(
        navigator = navigator,
        modifier = modifier,
        content = content,
        transitionModifier = { screen, transition, event ->
            val scale by transition.animateFloat(
                transitionSpec = { animationSpec },
                label = "ScaleTransition"
            ) { transitionScreen ->
                if (transitionScreen == screen) 1f
                else 0f
            }

            modifier.scale(
                if (transition.targetState == screen && event == StackEvent.Pop) 0.95f + (1f - 0.95f) * scale
                else 1.10f - (1.10f - 1f) * scale
            )
        }
    )
}
