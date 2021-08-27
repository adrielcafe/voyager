package cafe.adriel.voyager.core.lifecycle

import androidx.compose.runtime.ProvidedValue

public data class ScreenHooks(
    val providers: List<ProvidedValue<*>> = emptyList(),
    val disposer: () -> Unit = {}
) {

    internal companion object {
        val Empty = ScreenHooks()
    }
}
