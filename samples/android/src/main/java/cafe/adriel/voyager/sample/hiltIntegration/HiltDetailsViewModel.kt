package cafe.adriel.voyager.sample.hiltIntegration

import android.util.Log
import androidx.lifecycle.ViewModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel

@HiltViewModel(assistedFactory = HiltDetailsViewModel.Factory::class)
class HiltDetailsViewModel @AssistedInject constructor(
    @Assisted val index: Int
) : ViewModel() {

    override fun onCleared() {
        Log.d(">> TAG <<", "HiltDetailsViewModel cleared with index: $index")
    }

    @AssistedFactory
    interface Factory {
        fun create(index: Int): HiltDetailsViewModel
    }
}
