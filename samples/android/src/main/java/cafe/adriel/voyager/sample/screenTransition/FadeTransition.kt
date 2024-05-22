package cafe.adriel.voyager.sample.screenTransition

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import cafe.adriel.voyager.transitions.ScreenTransition

class FadeTransition : ScreenTransition {

    override fun enter(isPop: Boolean): EnterTransition {
        return fadeIn(tween(500, delayMillis = 500))
    }

    override fun exit(isPop: Boolean): ExitTransition {
        return fadeOut(tween(500))
    }
}
