package cafe.adriel.voyager.navigator.screenModel

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisallowComposableCalls
import androidx.compose.runtime.remember
import cafe.adriel.voyager.core.annotation.InternalVoyagerApi
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.ScreenModelStore
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.navigator.lifecycle.NavigatorDisposable
import cafe.adriel.voyager.navigator.lifecycle.NavigatorLifecycleStore

@Composable
public inline fun <reified T : ScreenModel> Navigator.rememberNavigatorScreenModel(
    tag: String? = null,
    crossinline factory: @DisallowComposableCalls () -> T
): T {
    // register the navigator lifecycle listener if is not already registered
    remember(this) {
        NavigatorLifecycleStore.register(this) { NavigatorScreenModelDisposer }
    }

    return remember(ScreenModelStore.getKey<T>(this.key, tag)) {
        ScreenModelStore.getOrPut(this.key, tag, factory)
    }
}

@InternalVoyagerApi
public object NavigatorScreenModelDisposer : NavigatorDisposable {
    override fun onDispose(navigator: Navigator) {
        ScreenModelStore.onDisposeNavigator(navigator.key)
    }
}
