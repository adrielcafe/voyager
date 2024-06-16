package cafe.adriel.voyager.sample.screenTransition

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOut
import androidx.compose.animation.slideOutVertically
import androidx.compose.ui.unit.IntOffset
import cafe.adriel.voyager.core.stack.StackEvent
import cafe.adriel.voyager.transitions.ScreenTransition

class FadeTransition : ScreenTransition {

    override fun enter(lastEvent: StackEvent): EnterTransition {
        return fadeIn(tween(500, delayMillis = 500))
    }

    override fun exit(lastEvent: StackEvent): ExitTransition {
        return fadeOut(tween(500))
    }
}

class SlideTransition : ScreenTransition {

    override fun enter(lastEvent: StackEvent): EnterTransition {
        return slideIn { size ->
            val x = if (lastEvent == StackEvent.Pop) -size.width else size.width
            IntOffset(x = x, y = 0)
        }
    }

    override fun exit(lastEvent: StackEvent): ExitTransition {
        return slideOut { size ->
            val x = if (lastEvent == StackEvent.Pop) size.width else -size.width
            IntOffset(x = x, y = 0)
        }
    }
}

class SlideInVerticallyTransition(val index: Int) : ScreenTransition {

    override fun enter(lastEvent: StackEvent): EnterTransition {
        return if (lastEvent == StackEvent.Pop) {
            fadeIn(initialAlpha = 0.9f)
        } else {
            slideInVertically { it }
        }
    }

    override fun exit(lastEvent: StackEvent): ExitTransition {
        return if (lastEvent == StackEvent.Pop) {
            slideOutVertically { it }
        } else {
            fadeOut(targetAlpha = 0.9f)
        }
    }

    override fun zIndex(lastEvent: StackEvent): Float {
        return if (lastEvent == StackEvent.Pop) (index).toFloat() else (index + 1).toFloat()
    }
}
