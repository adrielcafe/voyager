package cafe.adriel.voyager.dynamic.feature.details

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow

class DetailsScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        Column {
            Text(
                text = "Hey, you are in the Details now. Congratulations!!!!!",
                fontSize = 32.sp,
                modifier = Modifier
                    .background(color = Color.Cyan)
            )
            Text(
                text = "Well, just click to go back to Home",
                fontSize = 24.sp,
                modifier = Modifier
                    .background(color = Color.Red)
                    .clickable {
                        // Well, just navigate as you like
                        navigator.pop()
                    }
            )
        }
    }
}
