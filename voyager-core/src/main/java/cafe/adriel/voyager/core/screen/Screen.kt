package cafe.adriel.voyager.core.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
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

public interface Screen : Serializable {

    public val key: String
        get() = this::class.qualifiedName ?: error("Default key not found, please override it and set your own key")

    @Composable
    public fun Content()
}
