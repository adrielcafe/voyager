package cafe.adriel.voyager.sample.transition

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.uniqueScreenKey

data object ScaleScreen : Screen {
    override val key = uniqueScreenKey

    @Composable
    override fun Content() {
        Box(modifier = Modifier.fillMaxSize()) {
            Text(
                text = "Scale Screen",
                modifier = Modifier.align(alignment = Alignment.Center),
                color = Color.Red,
                fontSize = 30.sp
            )
        }
    }
}
