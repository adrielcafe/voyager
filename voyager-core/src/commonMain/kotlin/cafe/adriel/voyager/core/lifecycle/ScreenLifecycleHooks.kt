package cafe.adriel.voyager.core.lifecycle

import androidx.compose.runtime.ProvidedValue

public data class ScreenLifecycleHooks(
    val providers: List<ProvidedValue<*>> = emptyList(),
    val onDispose: () -> Unit = {}
) {

    internal companion object {
        val Empty = ScreenLifecycleHooks()
    }
}
