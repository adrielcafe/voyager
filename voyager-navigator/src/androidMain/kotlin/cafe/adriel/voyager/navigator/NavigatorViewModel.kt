package cafe.adriel.voyager.navigator

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.HasDefaultViewModelProviderFactory
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.compose.viewModel
import cafe.adriel.voyager.androidx.AndroidScreenLifecycleOwner
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.internal.NavigatorScreen

/**
 * Remember a [ViewModel] that will be scoped to current [LocalNavigator]
 *
 * @param key The key used to identify the [ViewModel]. Default is null
 * @param factory The [ViewModelProvider.Factory] that should be used to create the [ViewModel]. Default is null
 * @param extras The default extras used to create the [ViewModel].
 *
 * @return A [ViewModel] that is an instance of the given [T] type.
 */
@Suppress("UnusedReceiverParameter")
@Composable
public inline fun <reified T : ViewModel> Screen.rememberNavigatorViewModel(
    key: String? = null,
    factory: ViewModelProvider.Factory? = null,
    extras: CreationExtras? = null
): T {
    val navigator = LocalNavigator.currentOrThrow
    val navigatorScreen = remember(navigator) {
        NavigatorScreen(key = navigator.key)
    }
    // Having a screen with navigator key and put it to the AndroidScreenLifecycleOwner
    // it will have the same Navigator lifetime
    val storeOwner = AndroidScreenLifecycleOwner.get(navigatorScreen) as ViewModelStoreOwner
    return viewModel(
        key = key,
        factory = factory,
        viewModelStoreOwner = storeOwner,
        extras = when {
            extras != null -> extras
            storeOwner is HasDefaultViewModelProviderFactory -> storeOwner.defaultViewModelCreationExtras
            else -> CreationExtras.Empty
        }
    )
}
