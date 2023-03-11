package cafe.adriel.voyager.sample.androidLegacy

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.hilt.ScreenModelKey
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.multibindings.IntoMap

@Module
@InstallIn(ActivityComponent::class)
abstract class LegacyModule {
    @Binds
    @IntoMap
    @ScreenModelKey(LegacyOneScreenModel::class)
    abstract fun bindLegacyOneScreenModel(legacyOneScreenModel: LegacyOneScreenModel): ScreenModel

    @Binds
    @IntoMap
    @ScreenModelKey(LegacyTwoScreenModel::class)
    abstract fun bindLegacyTwoScreenModel(legacyTwoScreenModel: LegacyTwoScreenModel): ScreenModel
}
