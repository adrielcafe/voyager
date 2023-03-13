package cafe.adriel.voyager.hilt

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import cafe.adriel.voyager.androidx.AndroidScreenLifecycleOwner
import cafe.adriel.voyager.core.lifecycle.ScreenLifecycleProvider
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.hilt.internal.componentActivity

/**
 * A function to provide a [dagger.hilt.android.lifecycle.HiltViewModel] managed by voyager ViewModelLifecycleOwner
 * instead of using Activity ViewModelLifecycleOwner.
 * There is compatibility with Activity ViewModelLifecycleOwner too but it must be avoided because your ViewModels
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
    return remember(key1 = T::class) {
        val activity = context.componentActivity
        val lifecycleOwner = (this as? ScreenLifecycleProvider)
            ?.getLifecycleOwner() as? AndroidScreenLifecycleOwner
            ?: activity
        val factory = VoyagerHiltViewModelFactories.getVoyagerFactory(
            activity = activity,
            owner = lifecycleOwner,
            delegateFactory = viewModelProviderFactory ?: lifecycleOwner.defaultViewModelProviderFactory
        )
        val provider = ViewModelProvider(
            store = lifecycleOwner.viewModelStore,
            factory = factory,
            defaultCreationExtras = lifecycleOwner.defaultViewModelCreationExtras
        )
        provider[T::class.java]
    }
}
