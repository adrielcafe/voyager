package cafe.adriel.voyager.core.stack

public inline fun <reified I : Item, Item> Stack<Item>.popUntil(): Boolean =
    popUntil { item -> item is I }

public enum class StackEvent {
    Push,
    Replace,
    Pop,
    Idle
}

public interface PropertyHolderStack<Item> {

    public val items: List<Item>

    public val lastItemOrNull: Item?

    public val size: Int

    public val isEmpty: Boolean

    public val canPop: Boolean
}

public interface Stack<Item> : PropertyHolderStack<Item> {

    public val lastEvent: StackEvent

    public infix fun push(item: Item)

    public infix fun push(items: List<Item>)

    public infix fun replace(item: Item)

    public infix fun replaceAll(item: Item)

    public infix fun replaceAll(items: List<Item>)

    public infix fun popUntil(predicate: (Item) -> Boolean): Boolean

    public fun pop(): Boolean

    public fun popAll()

    public operator fun plusAssign(item: Item)

    public operator fun plusAssign(items: List<Item>)

    public fun clearEvent()
}

public data class StackLastAction<Item>(
    val invoker: Item?,
    val event: StackEvent,
)

/**
 * A [PropertyHolderStack] a stack that keeps track of the last action performed in it.
 * Crucial API difference from [Stack] is that this interface can't perform infix or operator functions.
 */
public interface WithLastActionStack<Item> : PropertyHolderStack<Item> {

    public val lastAction: StackLastAction<Item>

    public fun push(invoker: Item, item: Item)

    public fun push(invoker: Item, items: List<Item>)

    public fun replace(invoker: Item, item: Item)

    public fun replaceAll(invoker: Item, item: Item)

    public fun replaceAll(invoker: Item, items: List<Item>)

    public fun pop(invoker: Item): Boolean

    public fun popUntil(invoker: Item, predicate: (Item) -> Boolean): Boolean

    public fun popAll(invoker: Item)

    public fun clearEvent(invoker: Item)
}
