package cafe.adriel.voyager.core.model

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisallowComposableCalls
import androidx.compose.runtime.remember
import cafe.adriel.voyager.core.concurrent.PlatformMainDispatcher
import cafe.adriel.voyager.core.lifecycle.ScreenLifecycleStore
import cafe.adriel.voyager.core.screen.Screen
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

public val ScreenModel.screenModelScope: CoroutineScope
    get() = ScreenModelStore.getOrPutDependency(
        screenModel = this,
        name = "ScreenModelCoroutineScope",
        factory = { key -> CoroutineScope(SupervisorJob() + PlatformMainDispatcher + CoroutineName(key)) },
        onDispose = { scope -> scope.cancel() }
    )

@Composable
public inline fun <reified T : ScreenModel> Screen.rememberScreenModel(
    tag: String? = null,
    crossinline factory: @DisallowComposableCalls () -> T
): T {
    val screenModelStore = remember(this) {
        ScreenLifecycleStore.get(this) { ScreenModelStore }
    }
    return remember(screenModelStore.getKey<T>(this, tag)) {
        screenModelStore.getOrPut(this, tag, factory)
    }
}

public interface ScreenModel {

    public fun onDispose() {}
}

public abstract class StateScreenModel<S>(initialState: S) : ScreenModel {

    protected val mutableState: MutableStateFlow<S> = MutableStateFlow(initialState)
    public val state: StateFlow<S> = mutableState.asStateFlow()
}
