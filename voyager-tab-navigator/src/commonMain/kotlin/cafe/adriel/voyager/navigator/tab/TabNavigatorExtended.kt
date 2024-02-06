package cafe.adriel.voyager.navigator.tab

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator

public class TabNavigatorExtended internal constructor(
    internal val navigator: Navigator
) {

    public var current: Tab
        get() = navigator.lastItem as Tab
        private set(value) {}

    public fun setCurrent(invoker: Tab, newTab: Tab) {
        // TODO: sdfsdfd
        //navigator.replaceAll(invoker, newTab)
        navigator.replaceAll(newTab)
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

