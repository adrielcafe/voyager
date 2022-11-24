package cafe.adriel.voyager.core.lifecycle

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext

private tailrec fun Context.getActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.getActivity()
    else -> null
}

@Composable
internal actual fun getConfigurationChecker(): ConfigurationChecker {
    val context = LocalContext.current
    return remember(context) { ConfigurationChecker(context.getActivity()) }
}

internal actual class ConfigurationChecker(private val activity: Activity?) {
    actual fun isChangingConfigurations(): Boolean {
        return activity?.isChangingConfigurations ?: false
    }
}
