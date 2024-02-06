package cafe.adriel.voyager.navigator

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.neverEqualPolicy
import androidx.compose.runtime.saveable.SaveableStateHolder
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.core.annotation.ExperimentalVoyagerApi
import cafe.adriel.voyager.core.annotation.InternalVoyagerApi
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.stack.Stack
import cafe.adriel.voyager.core.stack.StackEvent
import cafe.adriel.voyager.core.stack.toMutableStateStack

public data class StackLastAction<Item>(
    val invoker: Item?,
    val event: StackEvent
)

public fun <Item> StackLastAction<Item>?.isPush(): Boolean = this?.event == StackEvent.Push
public fun <Item> StackLastAction<Item>?.isPop(): Boolean = this?.event == StackEvent.Pop
public fun <Item> StackLastAction<Item>?.isReplace(): Boolean = this?.event == StackEvent.Replace
public fun <Item> StackLastAction<Item>?.isIdle(): Boolean = this?.event == StackEvent.Idle

public class ExtendedNavigator @InternalVoyagerApi constructor(
    screens: List<Screen>,
    key: String,
    stateHolder: SaveableStateHolder,
    disposeBehavior: NavigatorDisposeBehavior,
    parent: Navigator? = null,
    private val stack: Stack<Screen> = screens.toMutableStateStack(minSize = 1)
) : Navigator(screens, key, stateHolder, disposeBehavior, parent, stack) {

    @ExperimentalVoyagerApi
    override var lastAction: StackLastAction<Screen>? by mutableStateOf(null, neverEqualPolicy())
    
    override fun push(item: Screen) {
        lastAction = StackLastAction(lastItemOrNull, StackEvent.Push)
        stack.push(item)
    }

    override fun push(items: List<Screen>) {
        lastAction = StackLastAction(lastItemOrNull, StackEvent.Push)
        stack.push(items)
    }

    override fun replace(item: Screen) {
        lastAction = StackLastAction(lastItemOrNull, StackEvent.Replace)
        stack.replace(item)
    }

    override fun replaceAll(item: Screen) {
        lastAction = StackLastAction(lastItemOrNull, StackEvent.Replace)
        stack.replaceAll(item)
    }

    override fun replaceAll(items: List<Screen>) {
        lastAction = StackLastAction(lastItemOrNull, StackEvent.Replace)
        stack.replaceAll(items)
    }

    override fun pop(): Boolean {
        if (canPop) {
            lastAction = StackLastAction(lastItemOrNull, StackEvent.Pop)
        }
        return stack.pop()
    }

    override fun popAll() {
        lastAction = StackLastAction(lastItemOrNull, StackEvent.Pop)
        stack.popAll()
    }

    override fun popUntil(predicate: (Screen) -> Boolean): Boolean {
        lastAction = StackLastAction(lastItemOrNull, StackEvent.Pop)
        return stack.popUntil(predicate)
    }

    override fun plusAssign(item: Screen) {
        lastAction = StackLastAction(lastItemOrNull, StackEvent.Push)
        stack.plusAssign(item)
    }

    override fun plusAssign(items: List<Screen>) {
        lastAction = StackLastAction(lastItemOrNull, StackEvent.Push)
        stack.plusAssign(items)
    }

    override fun clearEvent() {
        lastAction = StackLastAction(lastItemOrNull, StackEvent.Idle)
        stack.clearEvent()
    }
}
