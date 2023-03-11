package cafe.adriel.voyager.core.lifecycle

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember

private val configurationChecker = ConfigurationChecker()

@Composable
internal actual fun getConfigurationChecker(): ConfigurationChecker {
    return remember { configurationChecker }
}

@Stable
internal actual class ConfigurationChecker {
    actual fun isChangingConfigurations(): Boolean = false
}
