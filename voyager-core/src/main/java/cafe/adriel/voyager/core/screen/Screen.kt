package cafe.adriel.voyager.core.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import java.io.Serializable

@Composable
public fun Screen.LifecycleEffect(
    onStarted: () -> Unit = {},
    onDisposed: () -> Unit = {}
) {
    DisposableEffect(this) {
        onStarted()
        onDispose(onDisposed)
    }
}

public interface Screen : Serializable {

    @Composable
    public fun Content()
}
