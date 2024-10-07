package cafe.adriel.voyager.sample.screenModel

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import cafe.adriel.voyager.sample.shared.screenModel.ScreenModelSample

class ScreenModelActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ScreenModelSample()
        }
    }
}
