package cafe.adriel.voyager.navigator.internal

import android.os.Parcelable
import androidx.compose.runtime.saveable.listSaver
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.Navigator

/**
 * Navigator Saver that forces all Screens be [Parcelable], if not, it will throw a exception while trying to save
 * the navigator state.
 */
public fun parcelableNavigatorSaver(): NavigatorSaver<Any> = NavigatorSaver { _, stateHolder, disposeBehavior, parent ->
    listSaver(
        save = { navigator ->
            val screenAsParcelables = navigator.items.filterIsInstance<Parcelable>()

            if(navigator.items.size > screenAsParcelables.size) {
                val screensNotParcelable = navigator.items.filterNot { screen -> screenAsParcelables.any { screen == it } }
                    .map { it::class.simpleName }
                    .joinToString()

                throw VoyagerNavigatorSaverException("Unable to save instance state for Screens: $screensNotParcelable. Implement android.os.Parcelable on your Screen.")
            }

            screenAsParcelables
        },
        restore = { items -> Navigator(items as List<Screen>, stateHolder, disposeBehavior, parent) }
    )
}

public class VoyagerNavigatorSaverException(message: String) : RuntimeException(message)
