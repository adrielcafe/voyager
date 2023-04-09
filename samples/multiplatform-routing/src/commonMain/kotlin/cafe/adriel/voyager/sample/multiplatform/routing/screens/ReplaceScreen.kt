package cafe.adriel.voyager.sample.multiplatform.routing.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.routing.core.routing.VoyagerLocalRouter
import io.ktor.http.parametersOf

class ReplaceScreen(
    private val id: String? = null,
    private val q: String? = null,
) : Screen {
    private val actions = listOf(
        Action(
            text = "Pop screen",
            action = { it.pop() }
        ),
        Action(
            text = "Replace all a path",
            action = { it.replaceAll(path = "/replaceAll") }
        ),
        Action(
            text = "Replace all a path with parameters",
            action = { it.replaceAll(path = "/replaceAll/myId", parameters = parametersOf("q", "voyager")) }
        ),
        Action(
            text = "Replace all a name",
            action = { it.replaceAllNamed(name = "replaceAllWithoutParameters") }
        ),
        Action(
            text = "Replace all a name with parameters",
            action = {
                it.replaceAllNamed(
                    name = "replaceAllWithParameters",
                    parameters = parametersOf("q", "voyager"),
                    pathReplacements = parametersOf("id", "myId"),
                )
            }
        ),
    )

    @Composable
    override fun Content() {
        val router = VoyagerLocalRouter.currentOrThrow

        Column(
            modifier = Modifier.fillMaxSize().padding(10.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(text = "Welcome to Replace Screen")
            Spacer(modifier = Modifier.height(20.dp))

            Text(text = "Pop or back press from here will not return to last screen")
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
            Spacer(modifier = Modifier.height(15.dp))

            LazyColumn {
                items(actions) { item ->
                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            item.action(router)
                        }
                    ) {
                        Text(text = item.text)
                    }

                    if (item != actions.last()) {
                        Spacer(modifier = Modifier.height(15.dp))
                    }
                }
            }
        }
    }
}
