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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.rememberScreenModel
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

class BaseSampleScreenModel(
    val index: Int
) : ScreenModel {

    init {
        println("Init BaseSampleScreenModel $index")
    }
    override fun onDispose() {
        println("Disposing BaseSampleScreenModel $index")
    }
}

abstract class BaseSampleScreen(
    private val transitionType: String
) : Screen {

    abstract val index: Int

    override val key: ScreenKey get() = "SampleScreen$index"

    @Composable
    override fun Content() {
        val model = rememberScreenModel {
            BaseSampleScreenModel(index)
        }
        val color = remember {
            colors.getOrNull(index % colors.size) ?: colors.random()
        }
        val contentColor = remember {
            color.average()
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color)
                .padding(40.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Screen $index",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = contentColor
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = transitionType,
                fontSize = 18.sp,
                color = contentColor
            )
        }
    }
}

private fun Color.average(): Color {
    return Color((255 - red * 255) / 255, (255 - green * 255) / 255, (255 - blue * 255) / 255, alpha)
}

data class NoCustomAnimationSampleScreen(
    override val index: Int
) : BaseSampleScreen("Default transition")

data class FadeAnimationSampleScreen(
    override val index: Int
) : BaseSampleScreen("Fade transition"), ScreenTransition by FadeTransition()

data class SlideInVerticallyAnimationSampleScreen(
    override val index: Int
) : BaseSampleScreen("slide in vertically transition"), ScreenTransition by SlideInVerticallyTransition(index)
