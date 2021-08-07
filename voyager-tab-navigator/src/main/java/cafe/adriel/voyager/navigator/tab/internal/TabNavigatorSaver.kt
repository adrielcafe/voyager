package cafe.adriel.voyager.navigator.tab.internal

import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.SaveableStateHolder
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabNavigator

@Composable
internal fun rememberTabNavigator(
    tab: Tab
): TabNavigator {
    val stateHolder = rememberSaveableStateHolder()

    return rememberSaveable(saver = tabNavigatorSaver(stateHolder)) {
        TabNavigator(tab, stateHolder)
    }
}

private fun tabNavigatorSaver(
    stateHolder: SaveableStateHolder
): Saver<TabNavigator, Tab> =
    Saver(
        save = { tabNavigator -> tabNavigator.current },
        restore = { tab -> TabNavigator(tab, stateHolder) }
    )
