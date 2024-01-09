package cafe.adriel.voyager.core.model

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisallowComposableCalls
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.core.annotation.InternalVoyagerApi
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
        NavigatorLifecycleStore.get(this) { NavigatorScreenModelDisposer }
    }

    return remember(ScreenModelStore.getKey<T>(this.key, tag)) {
        ScreenModelStore.getOrPut(this.key, tag, factory)
    }
}

/**
 * Lookup for a [ScreenModel] that was remembered in the current [LocalNavigator] or in your parents.
 * If an instance is not found, an [IllegalStateException] will be thrown
 *
 * @param tag A custom tag used to "tag" the [ScreenModel]
 * @throws IllegalStateException if no [ScreenModel] is found in [LocalNavigator] or in your parents
 *
 * @return The [ScreenModel] instance found
 */
@Suppress("UnusedReceiverParameter")
@Throws(IllegalStateException::class)
@Composable
public inline fun <reified T : ScreenModel> Screen.navigatorScreenModel(
    tag: String? = null
): T {
    var navigator = LocalNavigator.currentOrThrow
    var screenModel: T? by remember(tag) { mutableStateOf(null) }
    while (screenModel == null) {
        screenModel = ScreenModelStore.getOrNull(
            holderKey = navigator.key,
            tag = tag
        )
        navigator = navigator.parent ?: break
    }
    return checkNotNull(screenModel) {
        "${T::class} was not found in $navigator and it parents"
    }
}

/**
 * Remember a [ScreenModel] that will be scoped to current [LocalNavigator]
 *
 * @param tag A custom tag used to "tag" the [ScreenModel]
 * @param factory A function to create a new instance if one is not remembered yet
 *
 * @return The [ScreenModel] instance
 */
@Suppress("UnusedReceiverParameter")
@Composable
public inline fun <reified T : ScreenModel> Screen.rememberNavigatorScreenModel(
    tag: String? = null,
    noinline factory: @DisallowComposableCalls () -> T
): T {
    val navigator = LocalNavigator.currentOrThrow
    return navigator.rememberNavigatorScreenModel(tag = tag, factory = factory)
}

@InternalVoyagerApi
public object NavigatorScreenModelDisposer : NavigatorDisposable {
    override fun onDispose(navigator: Navigator) {
        ScreenModelStore.onDisposeNavigator(navigator.key)
    }
}
