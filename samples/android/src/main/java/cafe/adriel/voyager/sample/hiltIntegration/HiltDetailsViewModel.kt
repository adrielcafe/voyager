package cafe.adriel.voyager.sample.hiltIntegration

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

// Until now, there is no support to assisted injection.
// Follow the thread here: https://github.com/google/dagger/issues/2287
class HiltDetailsViewModel(
    val index: Int
) : ViewModel() {

    override fun onCleared() {
        Log.d(">> TAG <<", "HiltDetailsViewModel#$index is being cleared by onCleared()")
    }

    companion object {
        fun provideFactory(
            index: Int
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T = HiltDetailsViewModel(index) as T
        }
    }
}
