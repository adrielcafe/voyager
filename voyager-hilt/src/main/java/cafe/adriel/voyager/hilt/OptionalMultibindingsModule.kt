package cafe.adriel.voyager.hilt

import cafe.adriel.voyager.core.model.ScreenModel
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.multibindings.Multibinds

/**
 * By default Dagger Multibindings are required to have at least one definition.
 * This module says to Dagger that a map with [ScreenModel] or [ScreenModelFactory] can be empty.
 */
@Module
@InstallIn(ActivityRetainedComponent::class)
public abstract class OptionalMultibindingsModule {
    @Multibinds
    public abstract fun screenModelFactories(): Map<Class<out ScreenModelFactory>, ScreenModelFactory>

    @Multibinds
    public abstract fun screenModels(): Map<Class<out ScreenModel>, ScreenModel>
}
