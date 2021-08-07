package cafe.adriel.voyager.sample.nestedNavigation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.CurrentScreen
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.NavigatorContent
import cafe.adriel.voyager.sample.basicNavigation.BasicNavigationScreen

class NestedNavigationActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            LazyColumn {
                item {
                    Content()
                }
            }
        }
    }

    @Composable
    private fun Content() {
        NestedNavigation(backgroundColor = Color.Gray) {
            CurrentScreen()
            NestedNavigation(backgroundColor = Color.LightGray) {
                CurrentScreen()
                NestedNavigation(backgroundColor = Color.White) { navigator ->
                    CurrentScreen()
                    Button(
                        onClick = { navigator.popUntilRoot() },
                        modifier = Modifier.padding(bottom = 16.dp)
                    ) {
                        Text(text = "Pop Until Root")
                    }
                }
            }
        }
    }

    @Composable
    private fun NestedNavigation(
        backgroundColor: Color,
        content: NavigatorContent = { CurrentScreen() }
    ) {
        Navigator(
            screen = BasicNavigationScreen(index = 0, wrapContent = true)
        ) { navigator ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(16.dp)
                    .background(backgroundColor)
            ) {
                Text(
                    text = "Level #${navigator.level}",
                    modifier = Modifier.padding(8.dp)
                )
                content(navigator)
            }
        }
    }
}
