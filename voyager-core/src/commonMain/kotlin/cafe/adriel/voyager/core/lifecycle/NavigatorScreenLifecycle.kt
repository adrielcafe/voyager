package cafe.adriel.voyager.core.lifecycle

import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf
import cafe.adriel.voyager.core.annotation.ExperimentalVoyagerApi
import cafe.adriel.voyager.core.screen.Screen

@ExperimentalVoyagerApi
public val LocalNavigatorScreenLifecycleProvider: ProvidableCompositionLocal<NavigatorScreenLifecycleProvider> =
    staticCompositionLocalOf { DefaultNavigatorScreenLifecycleProvider() }

/**
 * Can provides a list of ScreenLifecycleOwner for each Screen in the Navigator stack.
 */
@ExperimentalVoyagerApi
public interface NavigatorScreenLifecycleProvider {

    @ExperimentalVoyagerApi
    public fun provide(screen: Screen): List<ScreenLifecycleOwner>
}

internal expect class DefaultNavigatorScreenLifecycleProvider() : NavigatorScreenLifecycleProvider{
    override fun provide(screen: Screen): List<ScreenLifecycleOwner>
}

