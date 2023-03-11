package cafe.adriel.voyager.hilt

import cafe.adriel.voyager.core.model.ScreenModel
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import javax.inject.Provider

/**
 * A Hilt entry point that provide all [ScreenModelFactory] and [ScreenModel] in the graph.
 * To have all [ScreenModelFactory] and [ScreenModel] in the graph they must be declared using Multibinding
 */
@EntryPoint
@InstallIn(ActivityComponent::class)
public interface ScreenModelEntryPoint {

    /**
     * Provide all custom factories declared using multibinding
     */
    public fun screenModelFactories():
        Map<Class<out ScreenModelFactory>, @JvmSuppressWildcards Provider<ScreenModelFactory>>

    /**
     * Provide all screen models declared using multibinding
     */
    public fun screenModels(): Map<Class<out ScreenModel>, @JvmSuppressWildcards Provider<ScreenModel>>
}
