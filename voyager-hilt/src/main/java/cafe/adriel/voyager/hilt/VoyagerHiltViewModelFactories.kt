package cafe.adriel.voyager.hilt

import androidx.activity.ComponentActivity
import androidx.lifecycle.ViewModelProvider
import dagger.hilt.EntryPoint
import dagger.hilt.EntryPoints
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.internal.builders.ViewModelComponentBuilder
import dagger.hilt.android.internal.lifecycle.HiltViewModelFactory
import dagger.hilt.android.internal.lifecycle.HiltViewModelMap
import javax.inject.Inject

public object VoyagerHiltViewModelFactories {

    public fun getVoyagerFactory(
        activity: ComponentActivity,
        delegateFactory: ViewModelProvider.Factory
    ): ViewModelProvider.Factory {
        return EntryPoints.get(activity, ViewModelFactoryEntryPoint::class.java)
            .internalViewModelFactory()
            .fromActivity(delegateFactory)
    }

    internal class InternalViewModelFactory @Inject internal constructor(
        @HiltViewModelMap.KeySet private val keySet: Map<Class<*>, Boolean>,
        private val viewModelComponentBuilder: ViewModelComponentBuilder
    ) {
        fun fromActivity(
            delegateFactory: ViewModelProvider.Factory
        ): ViewModelProvider.Factory {
            return HiltViewModelFactory(keySet, delegateFactory, viewModelComponentBuilder)
        }
    }

    @EntryPoint
    @InstallIn(ActivityComponent::class)
    internal interface ViewModelFactoryEntryPoint {
        fun internalViewModelFactory(): InternalViewModelFactory
    }
}
