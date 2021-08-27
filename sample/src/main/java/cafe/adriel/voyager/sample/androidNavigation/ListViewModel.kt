package cafe.adriel.voyager.sample.androidNavigation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import java.util.UUID

class ListViewModel(private val handle: SavedStateHandle) : ViewModel() {

    init {
        handle["items"] = (0..100).map { "Item #$it | ${UUID.randomUUID().toString().substringBefore('-')}" }
    }

    val items: List<String>
        get() = handle["items"] ?: error("Items not found")
}
