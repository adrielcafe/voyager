package cafe.adriel.voyager.hilt

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.HasDefaultViewModelProviderFactory
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.CreationExtras
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
 * @param tag To identify the [ViewModel]
 * @param viewModelStoreOwner The scope that the [ViewModel] will be created in
 * @return A new instance of [ViewModel] or the existent instance in the [ViewModelStore]
 */
@Composable
public inline fun <reified T : ViewModel> Screen.getViewModel(
    viewModelProviderFactory: ViewModelProvider.Factory? = null,
    tag: String? = null,
    viewModelStoreOwner: ViewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current) {
        "No ViewModelStoreOwner was provided via LocalViewModelStoreOwner"
    }
): T {
    val context = LocalContext.current
    return remember(key1 = tag) {
        val factory = viewModelStoreOwner.createVoyagerFactory(context.componentActivity, viewModelProviderFactory)
        viewModelStoreOwner.get(T::class.java, tag, factory)
    }
}

/**
 * A function to provide a [dagger.hilt.android.lifecycle.HiltViewModel] managed by Voyager ViewModelLifecycleOwner
 * instead of using Activity ViewModelLifecycleOwner.
 * There is compatibility with Activity ViewModelLifecycleOwner too but it must be avoided because your ViewModels
 * will be cleared when activity is totally destroyed only.
 *
 * @param tag To identify the [ViewModel]
 * @param viewModelStoreOwner The scope that the [ViewModel] will be created in
 * @param viewModelFactory A custom factory to assist with creation of ViewModels
 * @return A new instance of [ViewModel] or the existent instance in the [ViewModelStore]
 */
@Composable
@ExperimentalVoyagerApi
public inline fun <reified T : ViewModel, F> Screen.getViewModel(
    tag: String? = null,
    viewModelStoreOwner: ViewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current) {
        "No ViewModelStoreOwner was provided via LocalViewModelStoreOwner"
    },
    noinline viewModelFactory: (F) -> T
): T {
    val context = LocalContext.current
    return remember(key1 = tag) {
        val factory = viewModelStoreOwner.createVoyagerFactory(context.componentActivity)
        viewModelStoreOwner.get(
            T::class.java,
            tag,
            factory,
            if (viewModelStoreOwner is HasDefaultViewModelProviderFactory) {
                viewModelStoreOwner.defaultViewModelCreationExtras.withCreationCallback(viewModelFactory)
            } else {
                CreationExtras.Empty.withCreationCallback(viewModelFactory)
            }
        )
    }
}

@PublishedApi
internal fun ViewModelStoreOwner.createVoyagerFactory(
    context: Context,
    viewModelProviderFactory: ViewModelProvider.Factory? = null
): ViewModelProvider.Factory? {
    val factory = viewModelProviderFactory
        ?: (this as? HasDefaultViewModelProviderFactory)?.defaultViewModelProviderFactory
    return if (factory != null) {
        VoyagerHiltViewModelFactories.getVoyagerFactory(
            activity = context.componentActivity,
            delegateFactory = factory
        )
    } else {
        null
    }
}

@PublishedApi
internal fun <T : ViewModel> ViewModelStoreOwner.get(
    javaClass: Class<T>,
    key: String?,
    viewModelProviderFactory: ViewModelProvider.Factory? = null,
    creationExtras: CreationExtras = if (this is HasDefaultViewModelProviderFactory) {
        this.defaultViewModelCreationExtras
    } else {
        CreationExtras.Empty
    }
): T {
    val factory = viewModelProviderFactory
        ?: (this as? HasDefaultViewModelProviderFactory)?.defaultViewModelProviderFactory
    val provider = if (factory != null) {
        ViewModelProvider(viewModelStore, factory, creationExtras)
    } else {
        ViewModelProvider(this)
    }
    return if (key != null) {
        provider[key, javaClass]
    } else {
        provider[javaClass]
    }
}
