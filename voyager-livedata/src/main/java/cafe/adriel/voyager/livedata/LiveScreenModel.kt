package cafe.adriel.voyager.livedata

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import cafe.adriel.voyager.core.model.ScreenModel

public abstract class LiveScreenModel<S>(initialState: S) : ScreenModel {

    protected val mutableState: MutableLiveData<S> = MutableLiveData(initialState)
    public val state: LiveData<S> = mutableState
}
