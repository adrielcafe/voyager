package cafe.adriel.voyager.sample.transitions

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import cafe.adriel.voyager.core.screen.uniqueScreenKey
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.FadeTransition

class TransitionNavigationActivity : ComponentActivity() {

    @ExperimentalAnimationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            Navigator(ScreenFoo()) { navigator ->
                Column(Modifier.fillMaxSize()) {
                    var enabled by remember { mutableStateOf(true) }
                    Box(
                        Modifier
                            .fillMaxSize()
                            .weight(1f)
                    ) {
                        FadeTransition(
                            navigator,
                            animationSpec = spring(
                                stiffness = Spring.StiffnessVeryLow,
                                visibilityThreshold = null
                            ),
                            onTransitionEnd = {
                                enabled = true
                            }
                        )
                    }
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                    ) {
                        Button(
                            enabled = enabled,
                            onClick = {
                                navigator.replace(ScreenFoo())
                                enabled = false
                            }
                        ) {
                            Text(text = "to FooScreen")
                        }
                        Spacer(Modifier.size(16.dp))
                        Button(
                            enabled = enabled,
                            onClick = {
                                navigator.replace(ScreenBar())
                                enabled = false
                            }
                        ) {
                            Text(text = "to BarScreen")
                        }
                    }
                }
            }
        }
    }
}

class ScreenFoo : Screen {
    @Composable
    override fun Content() {
        Box(
            Modifier
                .fillMaxSize()
                .background(Color.DarkGray),
            contentAlignment = Alignment.Center
        ) {
            Text("ScreenFoo")
        }
    }
}

class ScreenBar : Screen {

    override val key: ScreenKey
        get() = uniqueScreenKey

    @Composable
    override fun Content() {
        Box(
            Modifier
                .fillMaxSize()
                .background(Color.LightGray),
            contentAlignment = Alignment.Center
        ) {
            Text("ScreenBar")
        }
    }
}
