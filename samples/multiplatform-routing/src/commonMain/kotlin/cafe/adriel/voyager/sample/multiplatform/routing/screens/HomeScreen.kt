package cafe.adriel.voyager.sample.multiplatform.routing.screens

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.routing.core.routing.VoyagerLocalRouter
import cafe.adriel.voyager.routing.core.routing.VoyagerRouting
import io.ktor.http.parametersOf

internal data class Action(
    val text: String,
    val action: (VoyagerRouting) -> Unit,
)

class HomeScreen : Screen {
    private val actions = listOf(
        Action(
            text = "Push a path",
            action = { it.push(path = "/path") }
        ),
        Action(
            text = "Push a path with parameters",
            action = { it.push(path = "/path/myId", parameters = parametersOf("q", "voyager")) }
        ),
        Action(
            text = "Push a name",
            action = { it.pushNamed(name = "pathWithoutParameters") }
        ),
        Action(
            text = "Push a name with parameters",
            action = {
                it.pushNamed(
                    name = "pathWithParameters",
                    parameters = parametersOf("q", "voyager"),
                    pathReplacements = parametersOf("id", "myId"),
                )
            }
        ),
    )

    @Composable
    override fun Content() {
        val router = VoyagerLocalRouter.currentOrThrow

        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(10.dp)
        ) {
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
