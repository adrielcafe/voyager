package cafe.adriel.voyager.navigator

import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.saveable.SaveableStateHolder
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.staticCompositionLocalOf
import cafe.adriel.voyager.core.annotation.ExperimentalVoyagerApi
import cafe.adriel.voyager.core.screen.Screen

public val LocalNavigatorSaver: ProvidableCompositionLocal<NavigatorSaver<*>> =
    staticCompositionLocalOf { defaultNavigatorSaver() }

@ExperimentalVoyagerApi
public fun interface NavigatorSaver<Saveable : Any> {
    public fun saver(
        initialScreens: List<Screen>,
        key: String,
        stateHolder: SaveableStateHolder,
        disposeBehavior: NavigatorDisposeBehavior,
        parent: Navigator?
    ): Saver<Navigator, Saveable>
}

/**
 * Default Navigator Saver expect that on Android, your screens can be saved, by default
 * all [Screen]s are Java Serializable, this means that by default, if you only pass primitive types
 * or Java Serializable types, it will restore your screen properly.
 * If you want use Android Parcelable instead, you can, you just need to implement the Parcelable interface
 * and all types should be parcelable as well and this Default Saver should work as well.
 * Important: When using Parcelable all types should be Parcelable as well, when using Serializable all types
 * should be serializable, you can't mix both unless the types are both Parcelable and Serializable.
 *
 * If you want to use only Parcelable and want a NavigatorSaver that forces the use Parcelable, you can use [parcelableNavigatorSaver].
 */
public fun defaultNavigatorSaver(): NavigatorSaver<Any> =
    NavigatorSaver { _, key, stateHolder, disposeBehavior, parent ->
        listSaver(
            save = { navigator -> navigator.items },
            restore = { items -> Navigator(items, key, stateHolder, disposeBehavior, parent) }
        )
    }
