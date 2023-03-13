package cafe.adriel.voyager.sample.androidLegacy

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
import cafe.adriel.voyager.androidx.AndroidScreen
import cafe.adriel.voyager.hilt.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow

class LegacyScreenOne : AndroidScreen() {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val model: LegacyOneScreenModel = getScreenModel()

        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = model.text,
                style = MaterialTheme.typography.h5
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = model.toString().substringAfterLast('.'),
                style = MaterialTheme.typography.body2
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { navigator.push(LegacyScreenTwo()) },
                content = { Text(text = "Go to Two") }
            )
        }
    }
}
