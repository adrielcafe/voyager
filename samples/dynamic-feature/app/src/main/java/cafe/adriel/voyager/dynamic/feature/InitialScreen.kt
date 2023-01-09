package cafe.adriel.voyager.dynamic.feature

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.registry.rememberScreen
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.dynamic.feature.module.HomeDynamicFeatureScreenProvider
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow

internal object InitialScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        // Simulate navigation using ScreenRegistry
        val homeScreen = rememberScreen(provider = HomeDynamicFeatureScreenProvider)

        Column {
            Text(
                text = "Hey, I'm the app entry point. Congratulations!!!!!",
                fontSize = 32.sp,
                modifier = Modifier
                    .background(color = Color.Cyan)
            )
            Text(
                text = "Click me to go to Home",
                fontSize = 24.sp,
                modifier = Modifier
                    .background(color = Color.Red)
                    .clickable {
                        navigator.push(homeScreen)
                    }
            )
        }
    }
}
