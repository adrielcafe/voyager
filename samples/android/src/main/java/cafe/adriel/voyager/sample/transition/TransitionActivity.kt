package cafe.adriel.voyager.sample.transition

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.isPopLastEvent
import cafe.adriel.voyager.navigator.isPushLastEvent
import cafe.adriel.voyager.transitions.ScreenTransition
import cafe.adriel.voyager.transitions.ScreenTransitionContent

class TransitionActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            Navigator(TransitionScreen) {
                TransitionDemo(it)
            }
        }
    }
}

@Composable
fun TransitionDemo(
    navigator: Navigator,
    modifier: Modifier = Modifier,
    content: ScreenTransitionContent = { it.Content() }
) {
    val transition: AnimatedContentTransitionScope<Screen>.() -> ContentTransform = {
        // Define any StackEvent you want transition to be
        val isPush = navigator.isPushLastEvent()
        val isPop = navigator.isPopLastEvent()
        // Define any Screen you want transition must be from
        val invoker = this.initialState
        val isInvokerTransitionScreen = invoker == TransitionScreen
        val isInvokerFadeScreen = invoker == FadeScreen
        val isInvokerShrinkScreen = invoker == ShrinkScreen
        val isInvokerScaleScreen = invoker == ScaleScreen
        // Define any Screen you want transition must be to
        val target = this.targetState
        val isTargetTransitionScreen = target == TransitionScreen
        val isTargetFadeScreen = target == FadeScreen
        val isTargetShrinkScreen = target == ShrinkScreen
        val isTargetScaleScreen = target == ScaleScreen

        val tweenOffset: FiniteAnimationSpec<IntOffset> = tween(
            durationMillis = 2000,
            delayMillis = 100,
            easing = LinearEasing
        )
        val tweenSize: FiniteAnimationSpec<IntSize> = tween(
            durationMillis = 2000,
            delayMillis = 100,
            easing = LinearEasing
        )

        val sizeDefault = ({ size: Int -> size })
        val sizeMinus = ({ size: Int -> -size })
        val (initialOffset, targetOffset) = when {
            isPush && isInvokerTransitionScreen -> {
                if (isTargetFadeScreen || isTargetShrinkScreen) sizeMinus to sizeDefault
                else sizeDefault to sizeMinus
            }
            isPop && isInvokerFadeScreen && isTargetTransitionScreen -> sizeDefault to sizeMinus
            else -> sizeDefault to sizeMinus
        }

        val fadeInFrames = keyframes {
            durationMillis = 2000
            0.1f at 0 with LinearEasing
            0.2f at 1800 with LinearEasing
            1.0f at 2000 with LinearEasing
        }
        val fadeOutFrames = keyframes {
            durationMillis = 2000
            0.9f at 0 with LinearEasing
            0.8f at 100 with LinearEasing
            0.7f at 200 with LinearEasing
            0.6f at 300 with LinearEasing
            0.5f at 400 with LinearEasing
            0.4f at 500 with LinearEasing
            0.3f at 600 with LinearEasing
            0.2f at 1000 with LinearEasing
            0.1f at 1500 with LinearEasing
            0.0f at 2000 with LinearEasing
        }

        val scaleInFrames = keyframes {
            durationMillis = 2000
            0.1f at 0 with LinearEasing
            0.3f at 1500 with LinearEasing
            1.0f at 2000 with LinearEasing
        }
        val scaleOutFrames = keyframes {
            durationMillis = 2000
            0.9f at 0 with LinearEasing
            0.7f at 500 with LinearEasing
            0.3f at 700 with LinearEasing
            0.0f at 2000 with LinearEasing
        }

        when {
            // Define any transition you want based on the StackEvent, invoker and target
            isPush && isInvokerTransitionScreen && isTargetFadeScreen ||
                    isPop && isInvokerFadeScreen && isTargetTransitionScreen -> {
                val enter = slideInHorizontally(tweenOffset, initialOffset) + fadeIn(fadeInFrames)
                val exit = slideOutHorizontally(tweenOffset, targetOffset) + fadeOut(fadeOutFrames)
                enter togetherWith exit
            }
            isPush && isInvokerTransitionScreen && isTargetShrinkScreen ||
                isPop && isInvokerShrinkScreen && isTargetTransitionScreen -> {
                val enter = slideInVertically(tweenOffset, initialOffset)
                val exit = shrinkVertically(animationSpec = tweenSize, shrinkTowards = Alignment.Top)
                enter togetherWith exit
            }
            isPush && isInvokerTransitionScreen && isTargetScaleScreen -> {
                val enter = slideInVertically(tweenOffset, initialOffset) + fadeIn(fadeInFrames) + scaleIn(scaleInFrames)
                val exit = slideOutVertically(tweenOffset, targetOffset) + fadeOut(fadeOutFrames) + scaleOut(scaleOutFrames)
                enter togetherWith exit
            }
            isPop && isInvokerScaleScreen && isTargetTransitionScreen -> {
                val enter = slideInHorizontally(tweenOffset, initialOffset) + fadeIn(fadeInFrames) + scaleIn(scaleInFrames)
                val exit = slideOutHorizontally(tweenOffset, targetOffset) + fadeOut(fadeOutFrames) + scaleOut(scaleOutFrames)
                enter togetherWith exit
            }
            else -> {
                val animationSpec: FiniteAnimationSpec<IntOffset> = tween(
                    durationMillis = 500,
                    delayMillis = 100,
                    easing = LinearEasing
                )
                slideInHorizontally(animationSpec, initialOffset) togetherWith
                        slideOutHorizontally(animationSpec, targetOffset)
            }
        }
    }
    ScreenTransition(
        navigator = navigator,
        transition = transition,
        modifier = modifier,
        content = content,
    )
}
