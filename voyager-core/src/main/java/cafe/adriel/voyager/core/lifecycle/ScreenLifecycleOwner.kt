package cafe.adriel.voyager.core.lifecycle

import androidx.compose.runtime.Composable

public interface ScreenLifecycleOwner {

    @Composable
    public fun getHooks(): ScreenLifecycleHooks = ScreenLifecycleHooks.Empty
}

internal object DefaultScreenLifecycleOwner : ScreenLifecycleOwner
