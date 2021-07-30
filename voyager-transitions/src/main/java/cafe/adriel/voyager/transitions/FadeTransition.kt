package cafe.adriel.voyager.transitions

import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import cafe.adriel.voyager.navigator.Navigator

@Composable
public fun FadeTransition(
    navigator: Navigator,
    modifier: Modifier = Modifier,
    animationSpec: FiniteAnimationSpec<Float> = tween(),
    content: ScreenTransitionContent
) {
    ScreenTransition(
        navigator = navigator,
        modifier = modifier,
        content = content,
        transitionModifier = { screen, transition, _ ->
            val alpha by transition.animateFloat(
                transitionSpec = { animationSpec },
                label = "FadeTransition"
            ) { transitionScreen ->
                if (transitionScreen == screen) 1f
                else 0f
            }

            modifier.graphicsLayer {
                this.alpha = alpha
            }
        }
    )
}
