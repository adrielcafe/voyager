package cafe.adriel.voyager.sample.hiltIntegration

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

// Until now, there is no support to assisted injection.
// Follow the thread here: https://github.com/google/dagger/issues/2287
class HiltDetailsViewModel(
    val index: Int
) : ViewModel() {
    companion object {
        fun provideFactory(
            index: Int
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T = HiltDetailsViewModel(index) as T
        }
    }
}
