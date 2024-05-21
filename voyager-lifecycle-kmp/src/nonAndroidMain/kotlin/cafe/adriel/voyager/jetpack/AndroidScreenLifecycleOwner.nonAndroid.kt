package cafe.adriel.voyager.jetpack

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidedValue
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.MutableCreationExtras
import androidx.savedstate.SavedStateRegistryOwner

internal actual class SavedStateViewModelPlatform actual constructor(
    owner: SavedStateRegistryOwner
) {
    public actual fun getDefaultViewModelProviderFactory(): ViewModelProvider.Factory {
        return object : ViewModelProvider.Factory {

        }
    }

    public actual fun providePlatform(extras: MutableCreationExtras) {}

    @Composable
    public actual fun initHooks() {}

    public actual fun provideHooks(): List<ProvidedValue<*>> = emptyList()

}
