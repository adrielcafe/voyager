package cafe.adriel.voyager.sample.nestedNavigation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import cafe.adriel.voyager.sample.shared.nestedNavigation.NestedNavigationSample

class NestedNavigationActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            NestedNavigationSample()
        }
    }
}
