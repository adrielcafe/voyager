package cafe.adriel.voyager.hilt

import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import cafe.adriel.voyager.androidx.AndroidScreen
import cafe.adriel.voyager.androidx.AndroidScreenLifecycleOwner

@Composable
public inline fun <reified T : ViewModel> AndroidScreen.getViewModel(
    viewModelProviderFactory: ViewModelProvider.Factory? = null
): T {
    val context = LocalContext.current
    val androidScreenLifecycleOwner = getLifecycleOwner() as? AndroidScreenLifecycleOwner
        ?: throw IllegalStateException(
            "Invalid LifecycleOwner on AndroidScreen. It must be an AndroidScreenLifecycleOwner"
        )
    val componentActivity = context as? ComponentActivity
        ?: throw IllegalStateException("Invalid local context on AndroidScreen. It must be a ComponentActivity")
    val provider = ViewModelProvider(
        androidScreenLifecycleOwner.viewModelStore,
        viewModelProviderFactory ?: componentActivity.defaultViewModelProviderFactory
    )
    return provider[T::class.java]
}
