package cafe.adriel.voyager.dynamic.feature.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.dynamic.feature.custom.dynamicScreen
import cafe.adriel.voyager.dynamic.feature.module.DetailsDynamicFeatureScreenProvider
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow

class HomeScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        Column {
            Text(
                text = "Hey, you are in the Home. Welcome!!!",
                fontSize = 32.sp,
                modifier = Modifier
                    .background(color = Color.Cyan)
            )
            Text(
                text = "Click here to go to Details",
                fontSize = 24.sp,
                modifier = Modifier
                    .background(color = Color.Red)
                    .clickable {
                        // Simulate navigation without ScreenRegistry
                        navigator.push(dynamicScreen(screenProvider = DetailsDynamicFeatureScreenProvider))
                    }
            )
        }
    }
}
