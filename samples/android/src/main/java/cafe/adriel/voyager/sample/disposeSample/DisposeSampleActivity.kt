package cafe.adriel.voyager.sample.disposeSample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.DisposeStepsBehavior
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.NavigatorDisposeBehavior
import cafe.adriel.voyager.transitions.SlideTransition

open class DisposeSampleActivity(
    private val disposeStepsBehavior: DisposeStepsBehavior
) : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            Content()
        }
    }

    @Composable
    fun Content() {
        Navigator(
            screen = SampleScreen(0),
            disposeBehavior = NavigatorDisposeBehavior(
                disposeStepsBehavior = disposeStepsBehavior
            )
        ) {
            SlideTransition(
                navigator = it,
                animationSpec = tween(1000)
            )
        }
    }
}
