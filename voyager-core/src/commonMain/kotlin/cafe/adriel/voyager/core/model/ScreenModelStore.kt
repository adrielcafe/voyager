package cafe.adriel.voyager.core.model

import androidx.compose.runtime.DisallowComposableCalls
import cafe.adriel.voyager.core.annotation.InternalVoyagerApi
import cafe.adriel.voyager.core.concurrent.ThreadSafeMap
import cafe.adriel.voyager.core.lifecycle.ScreenDisposable
import cafe.adriel.voyager.core.platform.multiplatformName
import cafe.adriel.voyager.core.screen.Screen
import kotlinx.coroutines.flow.MutableStateFlow

private typealias ScreenModelKey = String

private typealias DependencyKey = String
private typealias DependencyInstance = Any
private typealias DependencyOnDispose = (Any) -> Unit
private typealias Dependency = Pair<DependencyInstance, DependencyOnDispose>

public object ScreenModelStore : ScreenDisposable {

    @PublishedApi
    internal val screenModels: MutableMap<ScreenModelKey, ScreenModel> = ThreadSafeMap()

    @PublishedApi
    internal val dependencies: MutableMap<DependencyKey, Dependency> = ThreadSafeMap()

    @PublishedApi
    internal val lastScreenModelKey: MutableStateFlow<ScreenModelKey?> = MutableStateFlow(null)

    @PublishedApi
    internal inline fun <reified T : ScreenModel> getKey(screen: Screen, tag: String?): ScreenModelKey =
        getKey<T>(screen.key, tag)

    /**
     * Public: used in Navigator Scoped ScreenModels
     */
    @InternalVoyagerApi
    public inline fun <reified T : ScreenModel> getKey(holderKey: String, tag: String?): ScreenModelKey =
        "${holderKey}:${T::class.multiplatformName}:${tag ?: "default"}"

    @PublishedApi
    internal fun getDependencyKey(screenModel: ScreenModel, name: String): DependencyKey =
        screenModels
            .firstNotNullOfOrNull {
                if (it.value == screenModel) {
                    it.key
                } else {
                    null
                }
            }
            ?: lastScreenModelKey.value
                ?.let { "$it:$name" }
            ?: "standalone:$name"

    @PublishedApi
    internal inline fun <reified T : ScreenModel> getOrPut(
        screen: Screen,
        tag: String?,
        factory: @DisallowComposableCalls () -> T
    ): T = getOrPut(screen.key, tag, factory)

    /**
     * Public: used in Navigator Scoped ScreenModels
     */
    @InternalVoyagerApi
    public inline fun <reified T : ScreenModel> getOrPut(
        holderKey: String,
        tag: String?,
        factory: @DisallowComposableCalls () -> T
    ): T {
        val key = getKey<T>(holderKey, tag)
        lastScreenModelKey.value = key
        return screenModels.getOrPut(key, factory) as T
    }

    public inline fun <reified T : Any> getOrPutDependency(
        screenModel: ScreenModel,
        name: String,
        noinline onDispose: @DisallowComposableCalls (T) -> Unit = {},
        noinline factory: @DisallowComposableCalls (DependencyKey) -> T
    ): T {
        val key = getDependencyKey(screenModel, name)

        return dependencies
            .getOrPut(key) { (factory(key) to onDispose) as Dependency }
            .first as T
    }

    override fun onDispose(screen: Screen) {
        disposeHolder(screen.key)
    }

    /**
     * Public: used in Navigator Scoped ScreenModels
     */
    @InternalVoyagerApi
    public fun onDisposeNavigator(navigatorKey: String) {
        disposeHolder(navigatorKey)
    }

    @Deprecated(
        message = "Use 'onDispose' instead. Will be removed in 1.0.0.",
        replaceWith = ReplaceWith("onDispose")
    )
    public fun remove(screen: Screen) {
        onDispose(screen)
    }

    private fun disposeHolder(holderKey: String) {
        screenModels.onEachHolder(holderKey) { key ->
            screenModels[key]?.onDispose()
            screenModels -= key
        }

        dependencies.onEachHolder(holderKey) { key ->
            dependencies[key]?.let { (instance, onDispose) -> onDispose(instance) }
            dependencies -= key
        }
    }


    private fun Map<String, *>.onEachHolder(holderKey: String, block: (String) -> Unit) =
        asSequence()
            .filter { it.key.startsWith(holderKey) }
            .map { it.key }
            .forEach(block)
}
