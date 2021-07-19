package cafe.adriel.voyager

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import java.io.Serializable

@Composable
public fun CurrentScreen() {
    LocalNavigator.currentOrThrow.last.Content()
}

@Composable
public fun Screen.LifecycleEffect(
    onStart: () -> Unit = {},
    onStop: () -> Unit = {}
) {
    DisposableEffect(this) {
        onStart()
        onDispose(onStop)
    }
}

public interface Screen : Serializable {

    @Composable
    public fun Content()
}
