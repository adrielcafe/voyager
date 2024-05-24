package cafe.adriel.voyager.sample.screenTransition

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import cafe.adriel.voyager.transitions.ScreenTransition

private val colors = listOf(
    Color.Red,
    Color.Yellow,
    Color.Green,
    Color.Blue,
    Color.Black
)

abstract class BaseSampleScreen(
    private val transitionType: String
) : Screen {

    abstract val index: Int

    override val key: ScreenKey get() = "SampleScreen$index"

    @Composable
    override fun Content() {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(colors[index % colors.size].copy(alpha = 0.3f))
                .padding(40.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Screen $index",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = transitionType,
                fontSize = 18.sp
            )
        }
    }
}

data class NoCustomAnimationSampleScreen(
    override val index: Int
) : BaseSampleScreen("Default transition")

data class FadeAnimationSampleScreen(
    override val index: Int
) : BaseSampleScreen("Fade transition"), ScreenTransition by FadeTransition()
