package cafe.adriel.voyager.sample.androidLegacy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.compose.ui.platform.ComposeView
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.sample.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LegacyActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_legacy)
        val composeView = findViewById<ComposeView>(R.id.composeView)
        composeView.setContent {
            Navigator(LegacyScreenOne())
        }
    }
}
