package cafe.adriel.voyager.navigator.lifecycle

import cafe.adriel.voyager.navigator.Navigator

public interface NavigatorDisposable {
    public fun onDispose(navigator: Navigator)
}
