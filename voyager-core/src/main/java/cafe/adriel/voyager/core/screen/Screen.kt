package cafe.adriel.voyager.core.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.ProvidedValue
import java.io.Serializable

@Composable
public fun Screen.LifecycleEffect(
    onStarted: () -> Unit = {},
    onDisposed: () -> Unit = {}
) {
    DisposableEffect(key) {
        onStarted()
        onDispose(onDisposed)
    }
}

public sealed class ScreenHook {
    public data class OnProvide<T>(val provide: () -> ProvidedValue<T>) : ScreenHook()
    public data class OnDispose(val dispose: () -> Unit) : ScreenHook()
}

public interface Screen : Serializable {

    public val key: String
        get() = this::class.qualifiedName ?: error("Default key not found, please provide your own key")

    public val hooks: List<ScreenHook>
        get() = emptyList()

    @Composable
    public fun Content()
}
