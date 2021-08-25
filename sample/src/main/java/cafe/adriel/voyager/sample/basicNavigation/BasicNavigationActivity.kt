package cafe.adriel.voyager.sample.basicNavigation

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition

class BasicNavigationActivity : ComponentActivity() {

    @OptIn(ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            Navigator(
                screen = BasicNavigationScreen(index = 0),
                onBackPressed = { currentScreen ->
                    Log.d("Navigator", "Pop screen #${(currentScreen as BasicNavigationScreen).index}")
                    true
                }
            ) { navigator ->
                SlideTransition(
                    navigator = navigator
                )
            }
        }
    }
}
