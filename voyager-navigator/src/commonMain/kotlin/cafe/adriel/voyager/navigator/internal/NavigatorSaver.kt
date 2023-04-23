package cafe.adriel.voyager.navigator.internal

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.SaveableStateHolder
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.staticCompositionLocalOf
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.NavigatorDisposeBehavior

internal val LocalNavigatorStateHolder: ProvidableCompositionLocal<SaveableStateHolder> =
    staticCompositionLocalOf { error("LocalNavigatorStateHolder not initialized") }

public val LocalNavigatorSaver: ProvidableCompositionLocal<NavigatorSaver<*>> =
    staticCompositionLocalOf { defaultNavigatorSaver() }

public fun interface NavigatorSaver<Saveable : Any> {
    public fun saver(
        initialScreens: List<Screen>,
        stateHolder: SaveableStateHolder,
        disposeBehavior: NavigatorDisposeBehavior,
        parent: Navigator?
    ): Saver<Navigator, Saveable>
}

@Composable
internal fun rememberNavigator(
    screens: List<Screen>,
    disposeBehavior: NavigatorDisposeBehavior,
    parent: Navigator?,
): Navigator {
    val stateHolder = LocalNavigatorStateHolder.current
    val navigatorSaver = LocalNavigatorSaver.current
    val saver = remember(navigatorSaver, stateHolder, parent, disposeBehavior) {
        navigatorSaver.saver(screens, stateHolder, disposeBehavior, parent)
    }

    return rememberSaveable(saver = saver) {
        Navigator(screens, stateHolder, disposeBehavior, parent)
    }
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
public fun defaultNavigatorSaver(): NavigatorSaver<Any> = NavigatorSaver { _, stateHolder, disposeBehavior, parent ->
    listSaver(
        save = { navigator -> navigator.items },
        restore = { items -> Navigator(items, stateHolder, disposeBehavior, parent) }
    )
}
