package cafe.adriel.voyager.hilt

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import cafe.adriel.voyager.androidx.AndroidScreen
import cafe.adriel.voyager.androidx.AndroidScreenLifecycleOwner

/**
 * A function to provide a [dagger.hilt.android.lifecycle.HiltViewModel] managed by voyager ViewModelStore
 * instead of using Activity ViewModelStore.
 *
 * @param viewModelProviderFactory A custom factory commonly used with Assisted Injection
 * @return A new instance of [ViewModel] or the existent instance in the [ViewModelStore]
 */
@Composable
public inline fun <reified T : ViewModel> AndroidScreen.getViewModel(
    viewModelProviderFactory: ViewModelProvider.Factory? = null
): T {
    val androidScreenLifecycleOwner = getLifecycleOwner() as? AndroidScreenLifecycleOwner
        ?: error("Invalid LifecycleOwner on AndroidScreen. It must be an AndroidScreenLifecycleOwner")
    val provider = ViewModelProvider(
        androidScreenLifecycleOwner.viewModelStore,
        viewModelProviderFactory ?: LocalContext.current.defaultViewModelProviderFactory
    )
    return provider[T::class.java]
}
