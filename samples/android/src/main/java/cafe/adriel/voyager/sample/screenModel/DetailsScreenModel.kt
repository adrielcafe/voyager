package cafe.adriel.voyager.sample.screenModel

import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class DetailsScreenModel(
    val index: Int
) : StateScreenModel<DetailsScreenModel.State>(State.Loading) {

    sealed class State {
        object Loading : State()
        data class Result(val item: String) : State()
    }

    fun getItem(index: Int) {
        coroutineScope.launch {
            delay(1_000)
            mutableState.value = State.Result("Item #$index")
        }
    }
}
