package cafe.adriel.voyager.jetpack

import androidx.compose.runtime.Composable
import androidx.lifecycle.HasDefaultViewModelProviderFactory
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.compose.viewModel
import cafe.adriel.voyager.core.annotation.ExperimentalVoyagerApi
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow

@ExperimentalVoyagerApi
@Composable
public fun Navigator.rememberNavigatorViewModelStoreOwner(): ViewModelStoreOwner {
    return rememberNavigatorJetpackOwner()
}

@ExperimentalVoyagerApi
@Composable
public inline fun <reified VM : ViewModel> navigatorViewModel(
    viewModelStoreOwner: ViewModelStoreOwner = LocalNavigator.currentOrThrow
        .rememberNavigatorViewModelStoreOwner(),
    key: String? = null,
    noinline initializer: CreationExtras.() -> VM
): VM {
    return viewModel(
        viewModelStoreOwner = viewModelStoreOwner,
        key = key,
        initializer = initializer
    )
}

@ExperimentalVoyagerApi
@Composable
public inline fun <reified VM : ViewModel> navigatorViewModel(
    viewModelStoreOwner: ViewModelStoreOwner = LocalNavigator.currentOrThrow
        .rememberNavigatorViewModelStoreOwner(),
    key: String? = null,
    factory: ViewModelProvider.Factory? = null,
    extras: CreationExtras = if (viewModelStoreOwner is HasDefaultViewModelProviderFactory) {
        viewModelStoreOwner.defaultViewModelCreationExtras
    } else {
        CreationExtras.Empty
    }
): VM = viewModel(
    viewModelStoreOwner = viewModelStoreOwner,
    key = key,
    factory = factory,
    extras = extras,
)
