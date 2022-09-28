package cafe.adriel.voyager.navigator.internal

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

@Composable
internal actual fun BackHandler(enabled: Boolean, onBack: () -> Unit) = Unit

@Composable
internal actual fun getConfigurationChecker(): ConfigurationChecker {
    return remember { ConfigurationChecker() }
}

internal actual class ConfigurationChecker {
    actual fun isChangingConfigurations(): Boolean = false
}
