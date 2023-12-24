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
import cafe.adriel.voyager.core.annotation.ExperimentalVoyagerApi
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.hilt.internal.componentActivity
import dagger.hilt.android.lifecycle.withCreationCallback

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
    val lifecycleOwner = LocalLifecycleOwner.current
    val viewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current) {
        "No ViewModelStoreOwner was provided via LocalViewModelStoreOwner"
    }
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

/**
 * A function to provide a [dagger.hilt.android.lifecycle.HiltViewModel] managed by Voyager ViewModelLifecycleOwner
 * instead of using Activity ViewModelLifecycleOwner.
 * There is compatibility with Activity ViewModelLifecycleOwner too but it must be avoided because your ViewModels
 * will be cleared when activity is totally destroyed only.
 *
 * @param viewModelProviderFactory A custom factory commonly used with Assisted Injection
 * @param viewModelFactory A custom factory to assist with creation of ViewModels
 * @return A new instance of [ViewModel] or the existent instance in the [ViewModelStore]
 */
@Composable
@ExperimentalVoyagerApi
public inline fun <reified VM : ViewModel, F> Screen.getViewModel(
    viewModelProviderFactory: ViewModelProvider.Factory? = null,
    noinline viewModelFactory: (F) -> VM
): VM {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val viewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current) {
        "No ViewModelStoreOwner was provided via LocalViewModelStoreOwner"
    }
    return remember(key1 = VM::class) {
        val hasDefaultViewModelProviderFactory = requireNotNull(lifecycleOwner as? HasDefaultViewModelProviderFactory) {
            "$lifecycleOwner is not a androidx.lifecycle.HasDefaultViewModelProviderFactory"
        }
        val viewModelStore = requireNotNull(viewModelStoreOwner?.viewModelStore) {
            "$viewModelStoreOwner is null or have a null viewModelStore"
        }

        val creationExtras = hasDefaultViewModelProviderFactory.defaultViewModelCreationExtras
            .withCreationCallback(viewModelFactory)

        val factory = VoyagerHiltViewModelFactories.getVoyagerFactory(
            activity = context.componentActivity,
            delegateFactory = viewModelProviderFactory
                ?: hasDefaultViewModelProviderFactory.defaultViewModelProviderFactory
        )

        val provider = ViewModelProvider(
            store = viewModelStore,
            factory = factory,
            defaultCreationExtras = creationExtras
        )

        provider[VM::class.java]
    }
}
