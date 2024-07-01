package cafe.adriel.voyager.navigator.internal

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.SaveableStateHolder
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.staticCompositionLocalOf
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigatorSaver
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.NavigatorCreator
import cafe.adriel.voyager.navigator.NavigatorDisposeBehavior
import cafe.adriel.voyager.navigator.summonNavigatorCreator

internal val LocalNavigatorStateHolder: ProvidableCompositionLocal<SaveableStateHolder> =
    staticCompositionLocalOf { error("LocalNavigatorStateHolder not initialized") }

@Composable
internal fun rememberNavigator(
    screens: List<Screen>,
    key: String,
    disposeBehavior: NavigatorDisposeBehavior,
    parent: Navigator?,
    navigatorCreator: NavigatorCreator = summonNavigatorCreator
): Navigator {
    val stateHolder = LocalNavigatorStateHolder.current
    val navigatorSaver = LocalNavigatorSaver.current
    val saver = remember(navigatorSaver, stateHolder, parent, disposeBehavior) {
        navigatorSaver.saver(screens, key, stateHolder, disposeBehavior, parent)
    }

    return rememberSaveable(saver = saver, key = key) {
        navigatorCreator(screens, key, stateHolder, disposeBehavior, parent)
    }
}
