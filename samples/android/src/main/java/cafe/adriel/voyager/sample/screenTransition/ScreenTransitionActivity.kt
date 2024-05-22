package cafe.adriel.voyager.sample.screenTransition

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.ScreenTransition

class ScreenTransitionActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            Content()
        }
    }

    @Composable
    fun Content() {
        Navigator(
            screen = NoCustomAnimationSampleScreen(0)
        ) {
            ScreenTransition(
                navigator = it,
                defaultTransition = SlideTransition()
            )
        }
    }
}
