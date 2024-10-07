package cafe.adriel.voyager.sample.shared.lifecycleViewModel

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.jetpack.ProvideNavigatorLifecycleKMPSupport
import cafe.adriel.voyager.navigator.Navigator

@Composable
fun LifecycleViewModelSample() {
    ProvideNavigatorLifecycleKMPSupport {
        Navigator(AndroidListScreen())
    }
}
