package cafe.adriel.voyager.navigator

import androidx.compose.runtime.saveable.SaveableStateHolder
import cafe.adriel.voyager.core.screen.Screen

public typealias NavigatorCreator = (
    screens: List<Screen>,
    key: String,
    stateHolder: SaveableStateHolder,
    disposeBehavior: NavigatorDisposeBehavior,
    parent: Navigator?
) -> Navigator

// Choose your Navigator
public var summonNavigatorCreator: NavigatorCreator = DefaultNavigatorCreator()

public fun DefaultNavigatorCreator(): NavigatorCreator = {
        screenCollection: List<Screen>,
        s: String,
        saveableStateHolder: SaveableStateHolder,
        navigatorDisposeBehavior: NavigatorDisposeBehavior,
        navigator: Navigator?, ->
    DefaultNavigator(screenCollection, s, saveableStateHolder, navigatorDisposeBehavior, navigator)
}
