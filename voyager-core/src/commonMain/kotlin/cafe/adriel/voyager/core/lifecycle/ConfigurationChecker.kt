package cafe.adriel.voyager.core.lifecycle

import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.Stable

@Composable
@NonRestartableComposable
internal expect fun getConfigurationChecker(): ConfigurationChecker

@Stable
internal expect class ConfigurationChecker {
    fun isChangingConfigurations(): Boolean
}
