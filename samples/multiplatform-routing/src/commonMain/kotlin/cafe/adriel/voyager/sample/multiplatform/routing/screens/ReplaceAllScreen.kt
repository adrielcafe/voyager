package cafe.adriel.voyager.sample.multiplatform.routing.screens

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
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.routing.core.routing.VoyagerLocalRouter

class ReplaceAllScreen(
    private val id: String? = null,
    private val q: String? = null,
) : Screen {

    @Composable
    override fun Content() {
        val router = VoyagerLocalRouter.currentOrThrow

        Column(
            modifier = Modifier.fillMaxSize().padding(10.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(text = "Welcome to Replace All Screen")
            Spacer(modifier = Modifier.height(20.dp))

            Text(text = "I am the root screen now")
            Spacer(modifier = Modifier.height(20.dp))

            Text(text = "Pop or back press here will close the application")
            Spacer(modifier = Modifier.height(20.dp))

            if (id.isNullOrBlank()) {
                Text(text = "To see your ID you have to navigate with parameters")
            } else {
                Text(text = "Congratulations!! Here your ID: $id")
            }
            Spacer(modifier = Modifier.height(15.dp))

            if (q.isNullOrBlank()) {
                Text(text = "To search for something you have to navigate with parameters")
            } else {
                Text(text = "Your are looking for: $q")
            }

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    router.replace(path = "/")
                }
            ) {
                Text(text = "Replace with Home")
            }
        }
    }
}
