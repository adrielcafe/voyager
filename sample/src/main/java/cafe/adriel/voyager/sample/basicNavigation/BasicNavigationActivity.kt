package cafe.adriel.voyager.sample.basicNavigation

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import cafe.adriel.voyager.navigator.Navigator

class BasicNavigationActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            Navigator(
                screen = BasicNavigationScreen(index = 0),
                onBackPressed = { currentScreen ->
                    Log.d("Navigator", "Pop screen #${(currentScreen as BasicNavigationScreen).index}")
                    true
                }
            )
        }
    }
}
