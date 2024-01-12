package cafe.adriel.voyager.core.stack

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.neverEqualPolicy
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue

public fun <Item> List<Item>.toMutableStateStack(
    minSize: Int = 0
): WithLastActionSnapshotStackState<Item> = WithLastActionSnapshotStackState(this, minSize)

public fun <Item> mutableStateStackOf(
    vararg items: Item,
    minSize: Int = 0
): WithLastActionSnapshotStackState<Item> = WithLastActionSnapshotStackState((items as List<Item>), minSize = minSize)

@Composable
public fun <Item : Any> rememberStateStack(
    vararg items: Item,
    minSize: Int = 0
): WithLastActionSnapshotStackState<Item> = rememberStateStack(items.toList(), minSize)

@Composable
public fun <Item : Any> rememberStateStack(
    items: List<Item>,
    minSize: Int = 0
): WithLastActionSnapshotStackState<Item> = rememberSaveable(saver = stackSaver(minSize)) {
    WithLastActionSnapshotStackState(items, minSize)
}

private fun <Item : Any> stackSaver(
    minSize: Int
): Saver<WithLastActionSnapshotStackState<Item>, Any> = listSaver(
    save = { stack -> stack.items },
    restore = { items -> WithLastActionSnapshotStackState(items, minSize) }
)

public class WithLastActionSnapshotStackState<Item>(
    items: List<Item>,
    minSize: Int = 1
) : SnapshotStatePropertyHolderStack<Item>(items, minSize), WithLastActionStack<Item> {

    init {
        require(minSize >= 1) { "SnapshotStackState can't work properly with none screen inside. Min size $minSize is less than one" }
    }

    public constructor(
        vararg items: Item,
        minSize: Int = 0
    ) : this(
        items = items.toList(),
        minSize = minSize
    )

    public override var lastAction: StackLastAction<Item> by mutableStateOf(
        value = StackLastAction(null, StackEvent.Idle), policy = neverEqualPolicy()
    )
    private set

    override fun push(invoker: Item, item: Item) {
        stateStack += item
        lastAction = StackLastAction(invoker, StackEvent.Push)
    }

    override fun push(invoker: Item, items: List<Item>) {
        stateStack += items
        lastAction = StackLastAction(invoker, StackEvent.Push)
    }

    override fun replace(invoker: Item, item: Item) {
        if (stateStack.isEmpty()) {
            push(invoker, item)
        } else {
            stateStack[stateStack.lastIndex] = item
        }
        lastAction = StackLastAction(invoker, StackEvent.Replace)
    }

    override fun replaceAll(invoker: Item, item: Item) {
        stateStack.clear()
        stateStack += item
        lastAction = StackLastAction(invoker, StackEvent.Replace)
    }

    override fun replaceAll(invoker: Item, items: List<Item>) {
        stateStack.clear()
        stateStack += items
        lastAction = StackLastAction(invoker, StackEvent.Replace)
    }

    override fun pop(invoker: Item): Boolean {
        return if (canPop) {
            stateStack.removeLast()
            lastAction = StackLastAction(invoker, StackEvent.Pop)
            true
        } else {
            false
        }
    }

    override fun popUntil(invoker: Item, predicate: (Item) -> Boolean): Boolean {
        var success = false
        val shouldPop = {
            lastItemOrNull
                ?.let(predicate)
                ?.also { success = it }
                ?.not()
                ?: false
        }

        while (canPop && shouldPop()) {
            stateStack.removeLast()
        }

        lastAction = StackLastAction(invoker, StackEvent.Pop)

        return success
    }

    override fun popAll(invoker: Item) {
        popUntil(invoker) { false }
    }

    override fun clearEvent(invoker: Item) {
        lastAction = StackLastAction(invoker, StackEvent.Idle)
    }
}
