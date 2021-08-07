package cafe.adriel.voyager.navigator.internal

import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.SaveableStateHolder
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.Navigator

@Composable
internal fun rememberNavigator(
    screens: List<Screen>,
    parent: Navigator?
): Navigator {
    val stateHolder = rememberSaveableStateHolder()

    return rememberSaveable(saver = navigatorSaver(stateHolder, parent)) {
        Navigator(screens, stateHolder, parent)
    }
}

private fun navigatorSaver(
    stateHolder: SaveableStateHolder,
    parent: Navigator?
): Saver<Navigator, Any> =
    listSaver(
        save = { navigator -> navigator.items },
        restore = { items -> Navigator(items, stateHolder, parent) }
    )
