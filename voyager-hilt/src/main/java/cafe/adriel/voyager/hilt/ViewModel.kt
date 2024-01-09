package cafe.adriel.voyager.hilt

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.HasDefaultViewModelProviderFactory
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import cafe.adriel.voyager.core.screen.Screen
import dagger.hilt.android.lifecycle.withCreationCallback

/**
 * A custom callback required by Hilt for assisted injection
 */
private typealias ViewModelFactory<VMF> = (VMF) -> ViewModel

/**
 * A function to provide a [dagger.hilt.android.lifecycle.HiltViewModel] managed by voyager [Screen] lifecycle
 * instead of using Activity or Fragment LifecycleOwner.
 *
 * @param key The key used to identify the [ViewModel]. Default is null
 * @param viewModelProviderFactory The [ViewModelProvider.Factory] that should be used to create the [ViewModel]
 * @param viewModelStoreOwner The owner of the [ViewModel] that controls the scope and lifetime
 * of the returned [ViewModel]. Defaults to using [LocalViewModelStoreOwner].
 * @param extras The default extras used to create the [ViewModel].
 *
 * @return A [ViewModel] that is an instance of the given [T] type.
 */
@Composable
public inline fun <reified T : ViewModel> Screen.hiltViewModel(
    key: String? = null,
    viewModelProviderFactory: ViewModelProvider.Factory? = null,
    viewModelStoreOwner: ViewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current) {
        "No ViewModelStoreOwner was provided via LocalViewModelStoreOwner"
    },
    extras: CreationExtras = if (viewModelStoreOwner is HasDefaultViewModelProviderFactory) {
        viewModelStoreOwner.defaultViewModelCreationExtras
    } else {
        CreationExtras.Empty
    }
): T = getViewModel<T, Nothing>(
    key = key,
    viewModelProviderFactory = viewModelProviderFactory,
    viewModelStoreOwner = viewModelStoreOwner,
    extras = extras
)

/**
 * A function to provide a [dagger.hilt.android.lifecycle.HiltViewModel] managed by voyager [Screen] lifecycle
 * instead of using Activity or Fragment LifecycleOwner.
 *
 * @param viewModelFactory A custom callback to be used for assisted injection
 * @param key The key used to identify the [ViewModel]. Default is null
 * @param viewModelStoreOwner The owner of the [ViewModel] that controls the scope and lifetime
 * of the returned [ViewModel]. Defaults to using [LocalViewModelStoreOwner].
 * @param extras The default extras used to create the [ViewModel].
 *
 * @return A [ViewModel] that is an instance of the given [T] type.
 */
@Composable
public inline fun <reified T : ViewModel, VMF> Screen.hiltViewModel(
    noinline viewModelFactory: ViewModelFactory<VMF>,
    key: String? = null,
    viewModelStoreOwner: ViewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current) {
        "No ViewModelStoreOwner was provided via LocalViewModelStoreOwner"
    },
    extras: CreationExtras = if (viewModelStoreOwner is HasDefaultViewModelProviderFactory) {
        viewModelStoreOwner.defaultViewModelCreationExtras
    } else {
        CreationExtras.Empty
    }
): T = getViewModel(
    key = key,
    viewModelFactory = viewModelFactory,
    viewModelStoreOwner = viewModelStoreOwner,
    extras = extras
)

/**
 * A function to provide a [dagger.hilt.android.lifecycle.HiltViewModel] managed by voyager [Screen] lifecycle
 * instead of using Activity or Fragment LifecycleOwner.
 *
 * @param key The key used to identify the [ViewModel]. Default is null
 * @param viewModelFactory A custom callback to be used for assisted injection
 * @param viewModelProviderFactory The [ViewModelProvider.Factory] that should be used to create the [ViewModel]
 * @param viewModelStoreOwner The owner of the [ViewModel] that controls the scope and lifetime
 * of the returned [ViewModel]. Defaults to using [LocalViewModelStoreOwner].
 * or null if you would like to use the default factory from the [LocalViewModelStoreOwner]
 * @param extras The default extras used to create the [ViewModel].
 *
 * @return A [ViewModel] that is an instance of the given [T] type.
 */
@Suppress("UnusedReceiverParameter")
@Composable
@PublishedApi
internal inline fun <reified T : ViewModel, VMF> Screen.getViewModel(
    key: String? = null,
    noinline viewModelFactory: ViewModelFactory<VMF>? = null,
    viewModelProviderFactory: ViewModelProvider.Factory? = null,
    viewModelStoreOwner: ViewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current) {
        "No ViewModelStoreOwner was provided via LocalViewModelStoreOwner"
    },
    extras: CreationExtras = if (viewModelStoreOwner is HasDefaultViewModelProviderFactory) {
        viewModelStoreOwner.defaultViewModelCreationExtras
    } else {
        CreationExtras.Empty
    }
): T {
    val delegateFactory = remember(key1 = key) {
        when {
            viewModelProviderFactory != null -> viewModelProviderFactory
            viewModelStoreOwner is HasDefaultViewModelProviderFactory ->
                viewModelStoreOwner.defaultViewModelProviderFactory
            else -> error(
                "A custom ViewModelProvider.Factory or your " +
                    "ViewModelStoreOwner be a HasDefaultViewModelProviderFactory is required"
            )
        }
    }
    val activity = getActivity()
    val factory = remember(key1 = key) {
        VoyagerHiltViewModelFactories.getVoyagerFactory(
            activity = activity,
            delegateFactory = delegateFactory
        )
    }
    return viewModel(
        key = key,
        factory = factory,
        viewModelStoreOwner = viewModelStoreOwner,
        extras = when {
            viewModelFactory == null -> extras
            else -> extras.withCreationCallback(viewModelFactory)
        }
    )
}
