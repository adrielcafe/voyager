package cafe.adriel.voyager.sample.androidViewModel

import androidx.lifecycle.ViewModel

class AndroidDetailsViewModel(
    val index: Int
) : ViewModel() {

    override fun onCleared() {
        println("ViewModel: clear details")
    }
}
