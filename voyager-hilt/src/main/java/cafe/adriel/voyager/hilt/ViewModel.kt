package cafe.adriel.voyager.hilt

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import cafe.adriel.voyager.core.lifecycle.ScreenLifecycleProvider
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.hilt.internal.componentActivity
import cafe.adriel.voyager.hilt.internal.defaultViewModelProviderFactory

/**
 * A function to provide a [dagger.hilt.android.lifecycle.HiltViewModel] managed by voyager ViewModelStore
 * instead of using Activity ViewModelStore.
 * There is compatibility with Activity ViewModelStore too but it must be avoided because your ViewModels
 * will be cleared when activity is totally destroyed only.
 *
 * @param viewModelProviderFactory A custom factory commonly used with Assisted Injection
 * @return A new instance of [ViewModel] or the existent instance in the [ViewModelStore]
 */
@Composable
public inline fun <reified T : ViewModel> Screen.getViewModel(
    viewModelProviderFactory: ViewModelProvider.Factory? = null
): T {
    val context = LocalContext.current
    val factory = viewModelProviderFactory ?: context.defaultViewModelProviderFactory
    return remember(key1 = T::class) {
        val viewModelStore = when (this) {
            is ScreenLifecycleProvider ->
                (this.getLifecycleOwner() as? ViewModelStoreOwner)?.viewModelStore
                    ?: error("LifecycleOwner provided by your Screen must be an androidx.lifecycle.ViewModelStoreOwner")
            else -> context.componentActivity.viewModelStore
        }
        val provider = ViewModelProvider(store = viewModelStore, factory = factory)
        provider[T::class.java]
    }
}
