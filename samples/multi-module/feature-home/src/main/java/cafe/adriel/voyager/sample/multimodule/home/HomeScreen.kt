package cafe.adriel.voyager.sample.multimodule.home

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
import cafe.adriel.voyager.core.registry.rememberScreen
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.sample.multimodule.navigation.SharedScreen
import java.util.UUID

class HomeScreen : Screen {

    private val randomId: String
        get() = UUID.randomUUID().toString()

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val postListScreen = rememberScreen(SharedScreen.PostList)
        val postDetailsScreen = rememberScreen(SharedScreen.PostDetails(id = randomId))

        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = "Home",
                style = MaterialTheme.typography.h5
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { navigator.push(postListScreen) }
            ) {
                Text(
                    text = "To Post List",
                    style = MaterialTheme.typography.button
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { navigator.push(postDetailsScreen) }
            ) {
                Text(
                    text = "To Post Details",
                    style = MaterialTheme.typography.button
                )
            }
        }
    }
}
