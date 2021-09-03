package cafe.adriel.voyager.rxjava

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.ScreenModelStore
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.subjects.BehaviorSubject

public val ScreenModel.disposables: CompositeDisposable
    get() = ScreenModelStore.getOrPutDependency(
        screenModel = this,
        name = "ScreenModelCompositeDisposable",
        factory = { CompositeDisposable() },
        onDispose = { disposables -> disposables.clear() }
    )

public abstract class RxScreenModel<T : Any> : ScreenModel {

    protected val mutableState: BehaviorSubject<T> = BehaviorSubject.create()
    public val state: Observable<T> = mutableState
}
