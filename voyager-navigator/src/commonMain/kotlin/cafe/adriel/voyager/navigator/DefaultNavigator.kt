package cafe.adriel.voyager.navigator

import androidx.compose.runtime.saveable.SaveableStateHolder
import cafe.adriel.voyager.core.annotation.InternalVoyagerApi
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.stack.Stack
import cafe.adriel.voyager.core.stack.toMutableStateStack

public class DefaultNavigator @InternalVoyagerApi constructor(
    screens: List<Screen>,
    key: String,
    stateHolder: SaveableStateHolder,
    disposeBehavior: NavigatorDisposeBehavior,
    parent: Navigator? = null,
    stack: Stack<Screen> = screens.toMutableStateStack(minSize = 1)
) : Navigator(screens, key, stateHolder, disposeBehavior, parent, stack)
