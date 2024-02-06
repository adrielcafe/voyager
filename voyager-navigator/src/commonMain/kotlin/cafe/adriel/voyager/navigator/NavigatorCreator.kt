package cafe.adriel.voyager.navigator

import androidx.compose.runtime.saveable.SaveableStateHolder
import cafe.adriel.voyager.core.annotation.ExperimentalVoyagerApi
import cafe.adriel.voyager.core.screen.Screen

public typealias NavigatorCreator = (
    screens: List<Screen>,
    key: String,
    stateHolder: SaveableStateHolder,
    disposeBehavior: NavigatorDisposeBehavior,
    parent: Navigator?
) -> Navigator

// Choose your Navigator
public fun summonNavigatorCreator(): NavigatorCreator = DefaultNavigatorCreator()

private fun DefaultNavigatorCreator(): NavigatorCreator = {
        screenCollection: List<Screen>,
        s: String,
        saveableStateHolder: SaveableStateHolder,
        navigatorDisposeBehavior: NavigatorDisposeBehavior,
        navigator: Navigator?, ->
    DefaultNavigator(screenCollection, s, saveableStateHolder, navigatorDisposeBehavior, navigator)
}

@ExperimentalVoyagerApi
private fun ExtendedNavigatorCreator(): NavigatorCreator = {
        screenCollection: List<Screen>,
        s: String,
        saveableStateHolder: SaveableStateHolder,
        navigatorDisposeBehavior: NavigatorDisposeBehavior,
        navigator: Navigator?, ->
    ExtendedNavigator(screenCollection, s, saveableStateHolder, navigatorDisposeBehavior, navigator)
}
