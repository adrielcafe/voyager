package cafe.adriel.voyager.jetpack

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import cafe.adriel.voyager.core.annotation.ExperimentalVoyagerApi
import cafe.adriel.voyager.core.annotation.InternalVoyagerApi
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.lifecycle.NavigatorDisposable
import cafe.adriel.voyager.navigator.lifecycle.NavigatorLifecycleStore

@InternalVoyagerApi
@ExperimentalVoyagerApi
@Composable
public fun Navigator.rememberNavigatorJetpackOwner(): VoyagerJetpackOwner {
    return remember(this) {
        NavigatorLifecycleStore.get(this) {
            NavigatorLifecycleJetpackOwner()
        }.owner
    }
}

@InternalVoyagerApi
@ExperimentalVoyagerApi
public class NavigatorLifecycleJetpackOwner : NavigatorDisposable {
    public val owner: VoyagerJetpackOwner = VoyagerJetpackOwner()

    override fun onDispose(navigator: Navigator) {
        owner.onDispose()
    }
}
