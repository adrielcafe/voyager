package cafe.adriel.voyager.sample.liveDataIntegration

import cafe.adriel.voyager.core.model.screenModelScope
import cafe.adriel.voyager.livedata.LiveScreenModel
import cafe.adriel.voyager.sample.sampleItems
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class LiveDataScreenModel : LiveScreenModel<LiveDataScreenModel.State>(State.Loading) {

    sealed class State {
        object Loading : State()
        data class Result(val items: List<String>) : State()
    }

    private val items = sampleItems

    fun getItems() {
        screenModelScope.launch {
            delay(1_000)
            mutableState.postValue(State.Result(items))
        }
    }
}
