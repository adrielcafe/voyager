package cafe.adriel.voyager.transitions

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.Navigator

@Composable
public fun CrossfadeTransition(
    navigator: Navigator,
    animationSpec: FiniteAnimationSpec<Float> = tween(),
    label: String = "Crossfade",
    modifier: Modifier = Modifier,
    content: @Composable (Screen) -> Unit = { it.Content() }
) {
    Crossfade(
        targetState = navigator.lastItem,
        animationSpec = animationSpec,
        modifier = modifier,
        label = label
    ) { screen ->
        navigator.saveableState("transition", screen) {
            content(screen)
        }
    }
}
