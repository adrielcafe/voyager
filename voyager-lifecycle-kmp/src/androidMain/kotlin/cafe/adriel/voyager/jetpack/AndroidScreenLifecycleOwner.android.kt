package cafe.adriel.voyager.jetpack

import android.app.Application
import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidedValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSavedStateRegistryOwner
import androidx.lifecycle.SavedStateViewModelFactory
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.MutableCreationExtras
import androidx.savedstate.SavedStateRegistryOwner
import java.util.concurrent.atomic.AtomicReference

internal actual class SavedStateViewModelPlatform actual constructor(val owner: SavedStateRegistryOwner) {
    private val atomicAppContext = AtomicReference<Context>()

    actual fun getDefaultViewModelProviderFactory(): ViewModelProvider.Factory =
        SavedStateViewModelFactory(
            application = atomicAppContext.get()?.getApplication(),
            owner = owner
        )

    actual fun providePlatform(extras: MutableCreationExtras) {
        val application = atomicAppContext.get()?.getApplication()
        if (application != null) {
            extras.set(ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY, application)
        }
    }

    @Composable
    actual fun initHooks() {
        atomicAppContext.compareAndSet(null, LocalContext.current.applicationContext)
    }

    actual fun provideHooks(): List<ProvidedValue<*>> = listOf(
        LocalSavedStateRegistryOwner provides owner,
        androidx.lifecycle.compose.LocalLifecycleOwner provides owner
    )

    private fun Context.getApplication(): Application? = when (this) {
        is Application -> this
        else -> null
    }
}
