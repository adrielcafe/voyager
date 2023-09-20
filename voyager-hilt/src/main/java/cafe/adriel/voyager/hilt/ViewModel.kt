package cafe.adriel.voyager.hilt

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.HasDefaultViewModelProviderFactory
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.hilt.internal.componentActivity

/**
 * A function to provide a [dagger.hilt.android.lifecycle.HiltViewModel] managed by the nearest [androidx.lifecycle.LifecycleOwner].
 * The nearest [androidx.lifecycle.LifecycleOwner] is provide by [LocalLifecycleOwner]. So, the look up will be:
 *
 * 1. Getting the nearest [androidx.lifecycle.LifecycleOwner] provided by [LocalLifecycleOwner]
 * 2. If there is no nearest and your [Screen] is an [cafe.adriel.voyager.androidx.AndroidScreen] than the [androidx.lifecycle.LifecycleOwner] will be [cafe.adriel.voyager.androidx.AndroidScreenLifecycleOwner]
 * 3. If not an [cafe.adriel.voyager.androidx.AndroidScreen] than will look up for the topmost [LocalLifecycleOwner] available
 *
 * To avoid instances living more than the screen lifecycle, please provide a custom [androidx.lifecycle.LifecycleOwner] or
 *
 * @param viewModelProviderFactory A custom factory commonly used with Assisted Injection
 * @return A new instance of [ViewModel] or the existent instance in the [ViewModelStore]
 */
@Suppress("UnusedReceiverParameter")
@Composable
public inline fun <reified T : ViewModel> Screen.getViewModel(
    viewModelProviderFactory: ViewModelProvider.Factory? = null
): T {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val viewModelStoreOwner = LocalViewModelStoreOwner.current
    return remember(key1 = T::class) {
        val hasDefaultViewModelProviderFactory = requireNotNull(lifecycleOwner as? HasDefaultViewModelProviderFactory) {
            "$lifecycleOwner is not a androidx.lifecycle.HasDefaultViewModelProviderFactory"
        }
        val viewModelStore = requireNotNull(viewModelStoreOwner?.viewModelStore) {
            "$viewModelStoreOwner is null or have a null viewModelStore"
        }
        val factory = VoyagerHiltViewModelFactories.getVoyagerFactory(
            activity = context.componentActivity,
            delegateFactory = viewModelProviderFactory
                ?: hasDefaultViewModelProviderFactory.defaultViewModelProviderFactory
        )
        val provider = ViewModelProvider(
            store = viewModelStore,
            factory = factory,
            defaultCreationExtras = hasDefaultViewModelProviderFactory.defaultViewModelCreationExtras
        )
        provider[T::class.java]
    }
}
