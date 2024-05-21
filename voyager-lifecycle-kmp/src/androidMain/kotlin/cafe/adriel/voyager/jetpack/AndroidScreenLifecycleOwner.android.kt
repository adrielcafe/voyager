package cafe.adriel.voyager.jetpack

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.ContextWrapper
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
    private val atomicContext = AtomicReference<Context>()

    actual fun getDefaultViewModelProviderFactory(): ViewModelProvider.Factory =
        SavedStateViewModelFactory(
            application = atomicContext.get()?.applicationContext?.getApplication(),
            owner = owner
        )

    actual fun providePlatform(extras: MutableCreationExtras) {
        val application = atomicContext.get()?.applicationContext?.getApplication()
        if (application != null) {
            extras.set(ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY, application)
        }
    }

    @Composable
    actual fun initHooks() {
        atomicContext.compareAndSet(null, LocalContext.current)
    }

    actual fun provideHooks(): List<ProvidedValue<*>> = listOf(
        LocalSavedStateRegistryOwner provides owner
    )

    actual fun isChangingConfigurations(): Boolean {
        val context = atomicContext.getAndSet(null) ?: return true
        val activity = context.getActivity()
        return activity != null && activity.isChangingConfigurations
    }

    private tailrec fun Context.getActivity(): Activity? = when (this) {
        is Activity -> this
        is ContextWrapper -> baseContext.getActivity()
        else -> null
    }

    private tailrec fun Context.getApplication(): Application? = when (this) {
        is Application -> this
        is ContextWrapper -> baseContext.getApplication()
        else -> null
    }
}
