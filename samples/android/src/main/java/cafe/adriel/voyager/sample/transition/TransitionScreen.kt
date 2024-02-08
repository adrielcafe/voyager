package cafe.adriel.voyager.sample.transition

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.uniqueScreenKey
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow

data object TransitionScreen : Screen {

    override val key = uniqueScreenKey

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            PushButton(text = "Push fade left\nPop fade right") {
                navigator.push(FadeScreen)
            }
            Spacer(modifier = Modifier.height(50.dp))
            PushButton(text = "Push shrink top\nPop shrink bottom") {
                navigator.push(ShrinkScreen)
            }
            Spacer(modifier = Modifier.height(50.dp))
            PushButton(text = "Push fade scale bottom\nPop scale right") {
                navigator.push(ScaleScreen)
            }
        }
    }
}

@Composable
private fun PushButton(
    text: String,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier.sizeIn(minWidth = 200.dp, minHeight = 70.dp)
    ) {
        Text(text = text)
    }
}
