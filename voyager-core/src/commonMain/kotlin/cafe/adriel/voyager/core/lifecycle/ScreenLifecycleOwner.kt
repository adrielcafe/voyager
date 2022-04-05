package cafe.adriel.voyager.core.lifecycle

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen

public interface ScreenLifecycleOwner {

    @Composable
    public fun getHooks(): ScreenLifecycleHooks = ScreenLifecycleHooks.Empty

    public fun onDispose(screen: Screen) {}
}

internal object DefaultScreenLifecycleOwner : ScreenLifecycleOwner
