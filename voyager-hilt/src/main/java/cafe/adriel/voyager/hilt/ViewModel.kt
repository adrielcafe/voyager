package cafe.adriel.voyager.hilt

import androidx.compose.runtime.Composable
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
 *
 * @param viewModelProviderFactory A custom factory commonly used with Assisted Injection
 * @return A new instance of [ViewModel] or the existent instance in the [ViewModelStore]
 */
@Composable
public inline fun <reified T : ViewModel> Screen.getViewModel(
    viewModelProviderFactory: ViewModelProvider.Factory? = null
): T {
    val context = LocalContext.current
    val factoryPromise = viewModelProviderFactory ?: context.defaultViewModelProviderFactory
    if (this is ScreenLifecycleProvider) {
        val viewModelStoreOwner = getLifecycleOwner() as? ViewModelStoreOwner
            ?: error("LifecycleOwner provided by your Screen must be an androidx.lifecycle.ViewModelStoreOwner")
        val provider = ViewModelProvider(
            store = viewModelStoreOwner.viewModelStore,
            factory = factoryPromise
        )
        return provider[T::class.java]
    }
    val viewModelStore = context.componentActivity.viewModelStore
    return ViewModelProvider(viewModelStore, factoryPromise)[T::class.java]
}
