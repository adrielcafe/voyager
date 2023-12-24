package cafe.adriel.voyager.sample.hiltIntegration

import androidx.lifecycle.ViewModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel

@HiltViewModel(assistedFactory = HiltDetailsViewModel.Factory::class)
class HiltDetailsViewModel @AssistedInject constructor(
    @Assisted val index: Int
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(index: Int): HiltDetailsViewModel
    }
}
