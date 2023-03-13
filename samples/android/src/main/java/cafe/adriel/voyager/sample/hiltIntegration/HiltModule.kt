package cafe.adriel.voyager.sample.hiltIntegration

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.hilt.ScreenModelFactory
import cafe.adriel.voyager.hilt.ScreenModelFactoryKey
import cafe.adriel.voyager.hilt.ScreenModelKey
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.multibindings.IntoMap

@Module
@InstallIn(ActivityComponent::class)
abstract class HiltModule {
    @Binds
    @IntoMap
    @ScreenModelKey(HiltListScreenModel::class)
    abstract fun bindHiltScreenModel(hiltListScreenModel: HiltListScreenModel): ScreenModel

    @Binds
    @IntoMap
    @ScreenModelFactoryKey(HiltDetailsScreenModel.Factory::class)
    abstract fun bindHiltDetailsScreenModelFactory(
        hiltDetailsScreenModelFactory: HiltDetailsScreenModel.Factory
    ): ScreenModelFactory
}
