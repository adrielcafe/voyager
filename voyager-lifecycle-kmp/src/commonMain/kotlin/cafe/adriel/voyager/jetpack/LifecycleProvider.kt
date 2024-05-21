package cafe.adriel.voyager.jetpack

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import cafe.adriel.voyager.core.annotation.ExperimentalVoyagerApi
import cafe.adriel.voyager.core.annotation.InternalVoyagerApi
import cafe.adriel.voyager.core.lifecycle.LocalNavigatorScreenLifecycleProvider
import cafe.adriel.voyager.core.lifecycle.NavigatorScreenLifecycleProvider
import cafe.adriel.voyager.core.lifecycle.ScreenLifecycleContentProvider
import cafe.adriel.voyager.core.lifecycle.ScreenLifecycleStore
import cafe.adriel.voyager.core.screen.Screen

@ExperimentalVoyagerApi
@Composable
public fun ProvideNavigatorLifecycleKMPSupport(
    content: @Composable () -> Unit
) {
    val provider = remember { JetpackSupportProvider() }
    CompositionLocalProvider(
        LocalNavigatorScreenLifecycleProvider provides provider
    ) {
        content()
    }
}

@InternalVoyagerApi
@ExperimentalVoyagerApi
public class JetpackSupportProvider : NavigatorScreenLifecycleProvider {
    override fun provide(screen: Screen): List<ScreenLifecycleContentProvider> =
        listOf(ScreenLifecycleStore.get(screen) { ScreenLifecycleKMPOwner() })

}
