package cafe.adriel.voyager.sample.shared.lifecycleViewModel

import androidx.lifecycle.ViewModel
import cafe.adriel.voyager.sample.shared.sampleItems

class AndroidListViewModel : ViewModel() {

    val items: List<String>
        get() = sampleItems

    override fun onCleared() {
        println("ViewModel: clear list")
    }
}
