package cafe.adriel.voyager.sample.screenTransition

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import cafe.adriel.voyager.sample.shared.screenTransition.ScreenTransitionSample

class ScreenTransitionActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ScreenTransitionSample()
        }
    }
}
