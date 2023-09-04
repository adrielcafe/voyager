package cafe.adriel.voyager.core.lifecycle

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.annotation.InternalVoyagerApi
import cafe.adriel.voyager.core.screen.Screen

public interface ScreenLifecycleOwner : ScreenLifecycleContentProvider, ScreenDisposable

public interface ScreenLifecycleContentProvider {
    /**
     * Called before rendering the Screen Content.
     *
     * IMPORTANT: This is only called when ScreenLifecycleOwner is provided by [ScreenLifecycleProvider] or [NavigatorScreenLifecycleProvider].
     */
    @Composable
    public fun ProvideBeforeScreenContent(
        provideSaveableState: @Composable (suffixKey: String, content: @Composable () -> Unit) -> Unit,
        content: @Composable () -> Unit
    ): Unit = content()
}

public interface ScreenDisposable {
    /**
     * Called on the Screen leaves the stack.
     */
    public fun onDispose(screen: Screen) {}
}

@InternalVoyagerApi
public object DefaultScreenLifecycleOwner : ScreenLifecycleOwner
