package cafe.adriel.voyager.sample.androidViewModel

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.sample.shared.lifecycleViewModel.AndroidListScreen
import cafe.adriel.voyager.sample.shared.lifecycleViewModel.LifecycleViewModelSample

class AndroidViewModelActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            LifecycleViewModelSample()
        }
    }
}
