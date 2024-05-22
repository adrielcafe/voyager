package cafe.adriel.voyager.sample.screenTransition

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideOut
import androidx.compose.ui.unit.IntOffset
import cafe.adriel.voyager.transitions.ScreenTransition

class SlideTransition : ScreenTransition {

    override fun enter(isPop: Boolean): EnterTransition {
        return slideIn { size ->
            val x = if (isPop) -size.width else size.width
            IntOffset(x = x, y = 0)
        }
    }

    override fun exit(isPop: Boolean): ExitTransition {
        return slideOut { size ->
            val x = if (isPop) size.width else -size.width
            IntOffset(x = x, y = 0)
        }
    }
}
