package cafe.adriel.voyager.core.lifecycle

import android.app.Activity
import androidx.activity.compose.LocalActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember

@Composable
@NonRestartableComposable
internal actual fun getConfigurationChecker(): ConfigurationChecker {
    val activity = LocalActivity.current
    return remember(activity) { ConfigurationChecker(activity) }
}

@Stable
internal actual class ConfigurationChecker(private val activity: Activity?) {
    actual fun isChangingConfigurations(): Boolean {
        return activity?.isChangingConfigurations ?: false
    }
}
