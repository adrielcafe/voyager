package cafe.adriel.voyager.core.lifecycle

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen

public interface ScreenLifecycleOwner {

    public val isCreated: Boolean

    @Composable
    public fun getHooks(): ScreenLifecycleHooks = ScreenLifecycleHooks.Empty

    public fun performSave(outState: SavedState) {}

    public fun onCreate(savedState: SavedState?) {}

    public fun onDispose(screen: Screen) {}

    public fun onStart() {}

    public fun onStop() {}

    public fun registerLifecycleListener(outState: SavedState) {}
}

internal object DefaultScreenLifecycleOwner : ScreenLifecycleOwner {
    override val isCreated: Boolean
        get() = true
}
