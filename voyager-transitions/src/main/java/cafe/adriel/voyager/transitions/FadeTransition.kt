package cafe.adriel.voyager.transitions

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.with
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.navigator.Navigator

@ExperimentalAnimationApi
@Composable
public fun FadeTransition(
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
        transition = { fadeIn(animationSpec = animationSpec) with fadeOut(animationSpec = animationSpec) }
    )
}
