package cafe.adriel.voyager.sample.basicNavigation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import cafe.adriel.voyager.sample.shared.basicNavigation.BasicNavigationSample

class BasicNavigationActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            BasicNavigationSample()
        }
    }
}
