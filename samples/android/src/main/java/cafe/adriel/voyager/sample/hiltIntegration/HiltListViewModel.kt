package cafe.adriel.voyager.sample.hiltIntegration

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import cafe.adriel.voyager.sample.sampleItems
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HiltListViewModel @Inject constructor(
    private val handle: SavedStateHandle
) : ViewModel() {

    init {
        if (handle.get<List<String>>("items").isNullOrEmpty()) {
            handle["items"] = sampleItems
        }
    }

    val items: List<String>
        get() = handle["items"] ?: error("Items not found")
}
