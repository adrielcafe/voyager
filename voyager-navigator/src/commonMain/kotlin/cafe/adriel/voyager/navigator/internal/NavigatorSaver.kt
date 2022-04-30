package cafe.adriel.voyager.navigator.internal

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidableCompositionLocal
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

@Composable
internal fun rememberNavigator(
    screens: List<Screen>,
    disposeBehavior: NavigatorDisposeBehavior,
    parent: Navigator?
): Navigator {
    val stateHolder = LocalNavigatorStateHolder.current

    return rememberSaveable(saver = navigatorSaver(stateHolder, disposeBehavior, parent)) {
        Navigator(screens, disposeBehavior, stateHolder, parent)
    }
}

private fun navigatorSaver(
    stateHolder: SaveableStateHolder,
    disposeBehavior: NavigatorDisposeBehavior,
    parent: Navigator?
): Saver<Navigator, Any> =
    listSaver(
        save = { navigator -> navigator.items },
        restore = { items -> Navigator(items, disposeBehavior, stateHolder, parent) }
    )
