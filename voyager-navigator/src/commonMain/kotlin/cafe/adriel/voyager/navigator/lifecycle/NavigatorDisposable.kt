package cafe.adriel.voyager.navigator.lifecycle

import cafe.adriel.voyager.core.annotation.ExperimentalVoyagerApi
import cafe.adriel.voyager.navigator.Navigator

@ExperimentalVoyagerApi
public interface NavigatorDisposable {
    public fun onDispose(navigator: Navigator)
}
