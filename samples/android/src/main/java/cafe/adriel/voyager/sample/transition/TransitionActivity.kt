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
import cafe.adriel.voyager.navigator.isReplaceLastEvent
import cafe.adriel.voyager.sample.tabNavigation.tabs.FavoritesTab
import cafe.adriel.voyager.sample.tabNavigation.tabs.HomeTab
import cafe.adriel.voyager.sample.tabNavigation.tabs.ProfileTab
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
        // Define any StackEvent you want transition should react to
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
        // Define offset based on target and invoker
        // Offset is important to choose side transition be to or from. Top, Left, Right, Bottom
        val sizeDefault = ({ size: Int -> size })
        val sizeMinus = ({ size: Int -> -size })
        val (initialOffset, targetOffset) = when {
            isPush -> {
                isInvokerTransitionScreen // in our example is always true
                // This reverts animation side.
                // FadeScreen will show from left
                // ShrinkScreen will show from top
                if (isTargetFadeScreen || isTargetShrinkScreen) sizeMinus to sizeDefault
                // Default Push behaviour.
                // Horizontal animation will show from right
                // Vertical animation will show from bottom
                else sizeDefault to sizeMinus  // Case when isInvokerScaleScreen
            }
            isPop -> {
                isTargetTransitionScreen  // in our example is always true
                // This reverts animation side.
                // TransitionScreen will show from right
                if (isInvokerFadeScreen) sizeDefault to sizeMinus
                // TransitionScreen will show from bottom
                else if (isInvokerShrinkScreen) sizeDefault to sizeMinus
                // Default Pop behaviour.
                // Horizontal animation will show from left
                // Vertical animation will show from top
                else sizeMinus to sizeDefault // Case when isInvokerScaleScreen
            }
            // Always the same side
            else -> sizeDefault to sizeMinus
        }
        // Create transitions
        val slide = TransitionSlide(initialOffset = initialOffset, targetOffset = targetOffset)
        val fade = TransitionFade
        val shrink = TransitionShrink
        val scale = TransitionScale
        // Define custom behaviour or use default
        // There can be any custom transition you want based on StackEvent, invoker and target
        when {
            isPush && isInvokerTransitionScreen && isTargetFadeScreen ||
                    isPop && isInvokerFadeScreen && isTargetTransitionScreen -> {
                val enter = slide.inHorizontally + fade.In
                val exit = slide.outHorizontally + fade.Out
                enter togetherWith exit
            }
            isPush && isInvokerTransitionScreen && isTargetShrinkScreen ||
                isPop && isInvokerShrinkScreen && isTargetTransitionScreen -> {
                val enter = slide.inVertically
                val exit = shrink.vertically
                enter togetherWith exit
            }
            isPush && isInvokerTransitionScreen && isTargetScaleScreen -> {
                val enter = slide.inVertically + fade.In + scale.In
                val exit = slide.outVertically + fade.Out + scale.Out
                enter togetherWith exit
            }
            isPop && isInvokerScaleScreen && isTargetTransitionScreen -> {
                val enter = slide.inHorizontally + fade.In + scale.In
                val exit = fade.Out + scale.Out
                enter togetherWith exit
            }
            // Default
            else -> {
                val slideShort = TransitionSlide(
                    initialOffset = initialOffset,
                    targetOffset = targetOffset,
                    animationSpec = TransitionTween.tweenOffsetShort
                )
                slideShort.inHorizontally togetherWith slideShort.outHorizontally
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

private object TransitionFrames {

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
}

@Composable
fun TransitionTab(
    navigator: Navigator,
    modifier: Modifier = Modifier,
    content: ScreenTransitionContent = { it.Content() }
) {
    val transition: AnimatedContentTransitionScope<Screen>.() -> ContentTransform = {
        // Define any StackEvent you want transition should react to
        val isReplace = navigator.isReplaceLastEvent() // in TabNavigator is always true
        // Define any Screen you want transition must be from
        val invoker = this.initialState
        val isInvokerHomeTab = invoker == HomeTab
        val isInvokerFavoritesTab = invoker == FavoritesTab
        val isInvokerProfileTab = invoker == ProfileTab
        // Define any Screen you want transition must be to
        val target = this.targetState
        val isTargetHomeTab = target == HomeTab
        val isTargetFavoritesTab = target == FavoritesTab
        val isTargetProfileTab = target == ProfileTab
        // Define offset based on target and invoker
        // Offset is important to choose side transition be to or from. Top, Left, Right, Bottom
        val sizeDefault = ({ size: Int -> size })
        val sizeMinus = ({ size: Int -> -size })
        val (initialOffset, targetOffset) = when {
            isReplace -> {  // in TabNavigator is always true
                // This reverts animation side.
                // Any else tabs will appear from the left
                if (isInvokerProfileTab) sizeMinus to sizeDefault
                // From center tab to the most left tab
                else if (isInvokerFavoritesTab && isTargetHomeTab) sizeMinus to sizeDefault
                // Default Push behaviour.
                // Horizontal animation will show from right
                // Vertical animation will show from bottom
                else sizeDefault to sizeMinus  // Case when isInvokerScaleScreen
            }
            // Always the same side
            else -> sizeDefault to sizeMinus
        }
        // Create transitions
        val slide = TransitionSlide(initialOffset = initialOffset, targetOffset = targetOffset)
        val fade = TransitionFade
        val shrink = TransitionShrink
        val scale = TransitionScale
        // Define custom behaviour or use default
        // There can be any custom transition you want based on StackEvent, invoker and target
        when {
            // From the most left tab to the most right tab
            isInvokerHomeTab && isTargetProfileTab -> {
                val enter = scale.In + fade.In
                val exit = scale.Out + fade.Out
                enter togetherWith exit
            }
            // From the most right tab to the center tab
            isInvokerProfileTab && isTargetFavoritesTab -> {
                val enter = slide.inVertically
                val exit = shrink.vertically
                enter togetherWith exit
            }
            // From the most right tab to the most left tab
            isInvokerProfileTab && isTargetHomeTab -> {
                val enter = scale.In + fade.In
                val exit = slide.outVertically
                enter togetherWith exit
            }
            // Default
            else -> {
                val slideShort = TransitionSlide(
                    initialOffset = initialOffset,
                    targetOffset = targetOffset,
                    animationSpec = TransitionTween.tweenOffsetShortest
                )
                slideShort.inHorizontally togetherWith slideShort.outHorizontally
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

private object TransitionTween {
    val tweenOffsetShortest: FiniteAnimationSpec<IntOffset> = tween(
        durationMillis = 200,
        delayMillis = 50,
        easing = LinearEasing
    )
    val tweenOffsetShort: FiniteAnimationSpec<IntOffset> = tween(
        durationMillis = 500,
        delayMillis = 100,
        easing = LinearEasing
    )
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
}

private class TransitionSlide(
    initialOffset: (Int) -> Int,
    targetOffset: (Int) -> Int,
    animationSpec: FiniteAnimationSpec<IntOffset> = TransitionTween.tweenOffset
) {
    val inHorizontally = slideInHorizontally(animationSpec, initialOffset)
    val outHorizontally = slideOutHorizontally(animationSpec, targetOffset)
    val inVertically = slideInVertically(animationSpec, initialOffset)
    val outVertically = slideOutVertically(animationSpec, targetOffset)
}

private object TransitionFade {
    val In = fadeIn(TransitionFrames.fadeInFrames)
    val Out = fadeOut(TransitionFrames.fadeOutFrames)
}

private object TransitionShrink {
    val vertically = shrinkVertically(animationSpec = TransitionTween.tweenSize, shrinkTowards = Alignment.Top)
}

private object TransitionScale {
    val In = scaleIn(TransitionFrames.scaleInFrames)
    val Out = scaleOut(TransitionFrames.scaleOutFrames)
}
