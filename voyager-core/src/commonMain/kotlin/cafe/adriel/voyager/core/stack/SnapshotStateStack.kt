package cafe.adriel.voyager.core.stack

import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.neverEqualPolicy
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList

public fun <Item> List<Item>.toMutableStateStack(
    minSize: Int = 0
): SnapshotStateStack<Item> =
    SnapshotStateStack(this, minSize)

public fun <Item> mutableStateStackOf(
    vararg items: Item,
    minSize: Int = 0
): SnapshotStateStack<Item> =
    SnapshotStateStack(*items, minSize = minSize)

@Composable
public fun <Item : Any> rememberStateStack(
    vararg items: Item,
    minSize: Int = 0
): SnapshotStateStack<Item> =
    rememberStateStack(items.toList(), minSize)

@Composable
public fun <Item : Any> rememberStateStack(
    items: List<Item>,
    minSize: Int = 0
): SnapshotStateStack<Item> =
    rememberSaveable(saver = stackSaver(minSize)) {
        SnapshotStateStack(items, minSize)
    }

private fun <Item : Any> stackSaver(
    minSize: Int
): Saver<SnapshotStateStack<Item>, Any> =
    listSaver(
        save = { stack -> stack.items },
        restore = { items -> SnapshotStateStack(items, minSize) }
    )

public class SnapshotStateStack<Item>(
    items: List<Item>,
    minSize: Int = 0
) : Stack<Item> {

    public constructor(
        vararg items: Item,
        minSize: Int = 0
    ) : this(
        items = items.toList(),
        minSize = minSize
    )

    init {
        require(minSize >= 0) { "Min size $minSize is less than zero" }
        require(items.size >= minSize) { "Stack size ${items.size} is less than the min size $minSize" }
    }

    @PublishedApi
    internal val stateStack: SnapshotStateList<Item> = items.toMutableStateList()

    public override var lastEvent: StackEvent by mutableStateOf(StackEvent.Idle, neverEqualPolicy())
        private set

    public override val items: List<Item> by derivedStateOf {
        stateStack.toList()
    }

    public override val lastItemOrNull: Item? by derivedStateOf {
        stateStack.lastOrNull()
    }

    public override val size: Int by derivedStateOf {
        stateStack.size
    }

    public override val isEmpty: Boolean by derivedStateOf {
        stateStack.isEmpty()
    }

    public override val canPop: Boolean by derivedStateOf {
        stateStack.size > minSize
    }

    public override infix fun push(item: Item) {
        stateStack += item
        lastEvent = StackEvent.Push
    }

    public override infix fun push(items: List<Item>) {
        stateStack += items
        lastEvent = StackEvent.Push
    }

    public override infix fun replace(item: Item) {
        if (stateStack.isEmpty()) {
            push(item)
        } else {
            stateStack[stateStack.lastIndex] = item
        }
        lastEvent = StackEvent.Replace
    }

    public override infix fun replaceAll(item: Item) {
        stateStack.clear()
        stateStack += item
        lastEvent = StackEvent.Replace
    }

    public override infix fun replaceAll(items: List<Item>) {
        stateStack.clear()
        stateStack += items
        lastEvent = StackEvent.Replace
    }

    public override fun pop(): Boolean =
        if (canPop) {
            stateStack.removeLast()
            lastEvent = StackEvent.Pop
            true
        } else {
            false
        }

    public override fun popAll() {
        popUntil { false }
    }

    public override infix fun popUntil(predicate: (Item) -> Boolean): Boolean {
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

        lastEvent = StackEvent.Pop

        return success
    }

    public override operator fun plusAssign(item: Item) {
        push(item)
    }

    public override operator fun plusAssign(items: List<Item>) {
        push(items)
    }

    override fun clearEvent() {
        lastEvent = StackEvent.Idle
    }
}
