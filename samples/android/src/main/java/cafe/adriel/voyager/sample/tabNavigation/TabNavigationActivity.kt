package cafe.adriel.voyager.sample.tabNavigation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import cafe.adriel.voyager.sample.shared.tabNavigation.TabNavigationSample

class TabNavigationActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            TabNavigationSample()
        }
    }
}
