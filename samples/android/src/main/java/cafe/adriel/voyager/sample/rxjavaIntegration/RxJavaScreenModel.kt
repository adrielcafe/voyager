package cafe.adriel.voyager.sample.rxJavaIntegration

import cafe.adriel.voyager.rxjava.RxScreenModel
import cafe.adriel.voyager.rxjava.disposables
import cafe.adriel.voyager.sample.sampleItems
import io.reactivex.rxjava3.core.Single
import java.util.concurrent.TimeUnit

class RxJavaScreenModel : RxScreenModel<RxJavaScreenModel.State>() {

    sealed class State {
        object Loading : State()
        data class Result(val items: List<String>) : State()
    }

    private val items = sampleItems

    fun getItems() {
        Single
            .just(items)
            .delay(1_000, TimeUnit.MILLISECONDS)
            .doOnSubscribe { mutableState.onNext(State.Loading) }
            .subscribe { items -> mutableState.onNext(State.Result(items)) }
            .let(disposables::add)
    }
}
