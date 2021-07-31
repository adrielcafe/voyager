package cafe.adriel.voyager.transitions

import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import cafe.adriel.voyager.core.stack.StackEvent
import cafe.adriel.voyager.navigator.Navigator

@Composable
public fun SlideTransition(
    navigator: Navigator,
    modifier: Modifier = Modifier,
    orientation: SlideOrientation = SlideOrientation.Horizontal,
    animationSpec: FiniteAnimationSpec<Float> = spring(stiffness = Spring.StiffnessLow),
    content: ScreenTransitionContent = { it.Content() }
) {
    BoxWithConstraints {
        ScreenTransition(
            navigator = navigator,
            modifier = modifier,
            content = content,
            transitionModifier = { screen, transition, event ->
                val offset = when (event) {
                    StackEvent.Pop -> -1
                    else -> 1
                }
                val size = when (orientation) {
                    SlideOrientation.Horizontal -> constraints.maxWidth
                    SlideOrientation.Vertical -> constraints.maxHeight
                }
                val translation by transition.animateFloat(
                    transitionSpec = { animationSpec },
                    label = "SlideTransition"
                ) { transitionScreen ->
                    if (transitionScreen == screen) 0f
                    else size.toFloat()
                }

                modifier.graphicsLayer {
                    val updatedTranslation =
                        if (transition.targetState == screen) offset * translation
                        else offset * -translation

                    when (orientation) {
                        SlideOrientation.Horizontal -> translationX = updatedTranslation
                        SlideOrientation.Vertical -> translationY = updatedTranslation
                    }
                }
            }
        )
    }
}

public enum class SlideOrientation {
    Horizontal,
    Vertical
}
