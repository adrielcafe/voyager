package cafe.adriel.voyager.sample.screenTransition

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow

abstract class BaseSampleScreen : Screen {

    abstract val index: Int

    override val key: ScreenKey get() = "SampleScreen$index"

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(40.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "Screen $index")

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    navigator.push(
                        when {
                            index % 2 == 0 -> NoCustomAnimationSampleScreen(index = index + 1)
                            else -> FadeAnimationSampleScreen(index = index + 1)
                        }
                    )
                }
            ) {
                Text(text = "Next")
            }

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = { navigator.pop() }
            ) {
                Text(text = "Back")
            }
        }
    }
}
