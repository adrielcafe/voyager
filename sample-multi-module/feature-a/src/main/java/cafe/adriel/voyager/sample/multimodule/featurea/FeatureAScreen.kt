package cafe.adriel.voyager.sample.multimodule.featurea

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.sample.multimodule.shared.SharedFeatureBWithParamScreen
import cafe.adriel.voyager.sample.multimodule.shared.SharedFeatureBWithoutParamScreen
import org.kodein.di.compose.rememberInstance
import java.util.UUID

class FeatureAScreen : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val sharedFeatureBWithoutParamScreen: SharedFeatureBWithoutParamScreen by rememberInstance()
        val sharedFeatureBWithParamScreen: SharedFeatureBWithParamScreen by rememberInstance(
            arg = SharedFeatureBWithParamScreen.Params(message = UUID.randomUUID().toString())
        )

        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = "Feature A",
                style = MaterialTheme.typography.h5
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { navigator.push(sharedFeatureBWithoutParamScreen) }
            ) {
                Text(
                    text = "To Feature B without param",
                    style = MaterialTheme.typography.button
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { navigator.push(sharedFeatureBWithParamScreen) }
            ) {
                Text(
                    text = "To Feature B with param",
                    style = MaterialTheme.typography.button
                )
            }
        }
    }
}
