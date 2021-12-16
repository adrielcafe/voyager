package cafe.adriel.voyager.hilt

import android.app.Application
import androidx.activity.ComponentActivity
import androidx.lifecycle.SavedStateViewModelFactory
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
        delegateFactory: ViewModelProvider.Factory?
    ): ViewModelProvider.Factory {
        return EntryPoints.get(activity, ViewModelFactoryEntryPoint::class.java)
            .internalViewModelFactory()
            .fromActivity(activity, owner, delegateFactory)
    }

    internal class InternalViewModelFactory @Inject internal constructor(
        private val application: Application,
        @HiltViewModelMap.KeySet private val keySet: Set<String>,
        private val viewModelComponentBuilder: ViewModelComponentBuilder
    ) {
        fun fromActivity(
            activity: ComponentActivity,
            owner: SavedStateRegistryOwner,
            delegateFactory: ViewModelProvider.Factory?
        ): ViewModelProvider.Factory {
            val defaultArgs = activity.intent?.extras
            val delegate = delegateFactory ?: SavedStateViewModelFactory(application, owner, defaultArgs)
            return HiltViewModelFactory(owner, defaultArgs, keySet, delegate, viewModelComponentBuilder)
        }
    }

    @EntryPoint
    @InstallIn(ActivityComponent::class)
    internal interface ViewModelFactoryEntryPoint {
        fun internalViewModelFactory(): InternalViewModelFactory
    }
}
