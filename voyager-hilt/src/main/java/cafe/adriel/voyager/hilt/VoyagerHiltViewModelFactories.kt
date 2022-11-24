package cafe.adriel.voyager.hilt

import androidx.activity.ComponentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.savedstate.SavedStateRegistryOwner
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
        owner: SavedStateRegistryOwner,
        delegateFactory: ViewModelProvider.Factory
    ): ViewModelProvider.Factory {
        return EntryPoints.get(activity, ViewModelFactoryEntryPoint::class.java)
            .internalViewModelFactory()
            .fromActivity(owner, delegateFactory)
    }

    internal class InternalViewModelFactory @Inject internal constructor(
        @HiltViewModelMap.KeySet private val keySet: Set<String>,
        private val viewModelComponentBuilder: ViewModelComponentBuilder
    ) {
        fun fromActivity(
            owner: SavedStateRegistryOwner,
            delegateFactory: ViewModelProvider.Factory
        ): ViewModelProvider.Factory {
            return HiltViewModelFactory(owner, null, keySet, delegateFactory, viewModelComponentBuilder)
        }
    }

    @EntryPoint
    @InstallIn(ActivityComponent::class)
    internal interface ViewModelFactoryEntryPoint {
        fun internalViewModelFactory(): InternalViewModelFactory
    }
}
