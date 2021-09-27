package cafe.adriel.voyager.hilt

import cafe.adriel.voyager.core.model.ScreenModel
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import javax.inject.Provider

/**
 * A hilt entry point that have factories and models provided by hilt graph
 */
@EntryPoint
@InstallIn(ActivityComponent::class)
public interface ScreenModelEntryPoint {
    /**
     * Provide all custom factories declared using multibinding
     */
    public fun screenFactories(): Map<Class<out ScreenFactory>, @JvmSuppressWildcards Provider<ScreenFactory>>

    /**
     * Provide all screen models declared using multibinding
     */
    public fun screenModels(): Map<Class<out ScreenModel>, @JvmSuppressWildcards Provider<ScreenModel>>
}
