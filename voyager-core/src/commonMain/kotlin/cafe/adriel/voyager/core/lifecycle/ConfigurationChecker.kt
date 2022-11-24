package cafe.adriel.voyager.core.lifecycle

import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable

@Composable
@NonRestartableComposable
internal expect fun getConfigurationChecker(): ConfigurationChecker

internal expect class ConfigurationChecker {
    fun isChangingConfigurations(): Boolean
}
