package cafe.adriel.voyager.navigator.internal

import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.Navigator

@Composable
internal fun rememberNavigator(
    screens: List<Screen>,
    parent: Navigator?
): Navigator =
    rememberSaveable(saver = navigatorSaver(parent)) {
        Navigator(screens, parent)
    }

private fun navigatorSaver(
    parent: Navigator?
): Saver<Navigator, Any> =
    listSaver(
        save = { navigator -> navigator.items },
        restore = { items -> Navigator(items, parent) }
    )
