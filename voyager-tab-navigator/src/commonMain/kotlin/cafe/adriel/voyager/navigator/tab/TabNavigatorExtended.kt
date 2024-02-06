package cafe.adriel.voyager.navigator.tab

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import cafe.adriel.voyager.core.lifecycle.DisposableEffectIgnoringConfiguration
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.NavigatorDisposeBehavior
import cafe.adriel.voyager.navigator.compositionUniqueId

import cafe.adriel.voyager.navigator.NavigatorExtended

public class TabNavigatorExtended internal constructor(
    internal val navigator: NavigatorExtended
) {

    public var current: Tab
        get() = navigator.lastItem as Tab
        private set(value) {}

    public fun setCurrent(invoker: Tab, newTab: Tab) {
        navigator.replaceAll(invoker, newTab)
        current = newTab
    }

    @Composable
    public fun saveableState(
        key: String,
        tab: Tab = current,
        content: @Composable () -> Unit
    ) {
        navigator.saveableState(key, tab, content = content)
    }
}

