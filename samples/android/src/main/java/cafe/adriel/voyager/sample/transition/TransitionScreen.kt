package cafe.adriel.voyager.sample.transition

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.uniqueScreenKey
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow

data object TransitionScreen : Screen {

    override val key = uniqueScreenKey

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp)
        ) {
            Frame(
                text = "Navigator",
                borderColor = Color.Blue
            ) {
                PushButton(
                    currentBehaviourMessage = "1) on Push - slide to Right + fade out\n2) on Pop - reverse",
                    targetBehaviourMessage = "1) on Push slide from Left + fade in\n2) on Pop - reverse"
                ) {
                    navigator.push(FadeScreen)
                }
                Spacer(modifier = Modifier.height(20.dp))
                PushButton(
                    currentBehaviourMessage = "Current screen:\n1) on Push - shrink vertically to top\n2) on Pop - slide from bottom",
                    targetBehaviourMessage = "1) on Push slide from top\n2) on Pop - shrink vertically to top"
                ) {
                    navigator.push(ShrinkScreen)
                }
                Spacer(modifier = Modifier.height(20.dp))
                PushButton(
                    currentBehaviourMessage = "1) on Push - slide to top + fade out + scale out\n2) on Pop - slide from left + fade in + scale in",
                    targetBehaviourMessage = "1) on Push slide from bottom + scale in\n2) on Pop - fade out + scale out"
                ) {
                    navigator.push(ScaleScreen)
                }
            }
            Spacer(modifier = Modifier.height(50.dp))
            Frame(
                text = "TabNavigator",
                borderColor = Color.Magenta
            ) {
                Button(
                    onClick = { navigator.push(TabNavigationScreen) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .sizeIn(minHeight = 70.dp)
                        .padding(horizontal = 32.dp)
                ) {
                    Text(text = "Tap to see how transition might work inside TabNavigator")
                }
            }
        }
    }
}

@Composable
private fun ColumnScope.Frame(
    text: String,
    borderColor: Color,
    content: @Composable () -> Unit
) {
    Text(
        text = text,
        modifier = Modifier.align(Alignment.Start),
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold
    )
    Spacer(modifier = Modifier.height(8.dp))
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 3.dp,
                color = borderColor,
                shape = RoundedCornerShape(size = 10.dp)
            )
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        content()
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun PushButton(
    currentBehaviourMessage: String = "",
    targetBehaviourMessage: String = "",
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.Start,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Current screen:",
                color = Color.Cyan,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = currentBehaviourMessage,
                fontSize = 12.sp
            )
            Text(
                text = "Target screen:",
                color = Color.Green,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = targetBehaviourMessage,
                fontSize = 12.sp
            )
        }
    }
}
