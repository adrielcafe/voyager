package cafe.adriel.voyager.kodein

import cafe.adriel.voyager.core.annotation.ExperimentalVoyagerApi
import cafe.adriel.voyager.core.lifecycle.ScreenLifecycleOwner
import cafe.adriel.voyager.core.lifecycle.ScreenLifecycleStore
import cafe.adriel.voyager.core.screen.Screen
import org.kodein.di.bindings.Scope
import org.kodein.di.bindings.ScopeRegistry
import org.kodein.di.bindings.StandardScopeRegistry
import org.kodein.di.internal.synchronizedIfNull

@ExperimentalVoyagerApi
public class ScreenContext(
    public val screen: Screen
) {
    internal var disposeCallback: () -> Unit = {}

    internal fun onRegistryScope() {
        ScreenLifecycleStore.register(screen) {
            ScreenScopeLifecycleOwner(disposeCallback)
        }
    }

    override fun hashCode(): Int = screen.key.hashCode()

    override fun equals(other: Any?): Boolean =
        (other as? Screen?)?.key == screen.key
}

@ExperimentalVoyagerApi
public open class ScreenLifecycleScope private constructor(
    private val newRegistry: () -> ScopeRegistry
) : Scope<ScreenContext> {

    @ExperimentalVoyagerApi
    public companion object multiItem : ScreenLifecycleScope(::StandardScopeRegistry)

    private val map = HashMap<ScreenContext, ScopeRegistry>()

    override fun getRegistry(context: ScreenContext): ScopeRegistry {
        return synchronizedIfNull(
            lock = map,
            predicate = { map[context] },
            ifNotNull = { it },
            ifNull = {
                val registry = newRegistry()
                map[context] = registry
                context.disposeCallback = {
                    registry.clear()
                    map.remove(context)
                }
                context.onRegistryScope()
                registry
            }
        )
    }
}

private class ScreenScopeLifecycleOwner(
    val onDispose: () -> Unit
) : ScreenLifecycleOwner {
    override fun onDispose(screen: Screen) {
        onDispose()
    }
}
