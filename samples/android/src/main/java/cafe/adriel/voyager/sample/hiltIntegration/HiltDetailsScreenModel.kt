package cafe.adriel.voyager.sample.hiltIntegration

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.hilt.ScreenModelFactory
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

// Working with Assisted Injection here to simulate a custom param in the constructor
class HiltDetailsScreenModel @AssistedInject constructor(
    @Assisted val index: Int
) : ScreenModel {

    @AssistedFactory
    interface Factory : ScreenModelFactory {
        fun create(index: Int): HiltDetailsScreenModel
    }
}
