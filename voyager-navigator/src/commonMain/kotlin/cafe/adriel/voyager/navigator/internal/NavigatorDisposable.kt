package cafe.adriel.voyager.navigator.internal

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import cafe.adriel.voyager.core.stack.StackEvent
import cafe.adriel.voyager.navigator.Navigator

@Composable
internal expect fun getConfigurationChecker(): ConfigurationChecker

internal expect class ConfigurationChecker {
    fun isChangingConfigurations(): Boolean
}

private val disposableEvents: Set<StackEvent> =
    setOf(StackEvent.Pop, StackEvent.Replace)

@Composable
internal fun NavigatorDisposableEffect(
    navigator: Navigator
) {
    val configurationChecker = getConfigurationChecker()
    DisposableEffect(navigator) {
        onDispose {
            if (configurationChecker.isChangingConfigurations()) return@onDispose
            for (screen in navigator.items) {
                navigator.dispose(screen)
            }
            navigator.clearEvent()
        }
    }
}

@Composable
internal fun StepDisposableEffect(
    navigator: Navigator
) {
    val currentScreen = navigator.lastItem

    DisposableEffect(currentScreen.key) {
        onDispose {
            if (navigator.lastEvent in disposableEvents) {
                navigator.dispose(currentScreen)
                navigator.clearEvent()
            }
        }
    }
}
