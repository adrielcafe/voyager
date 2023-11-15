package cafe.adriel.voyager.core.stack

public inline fun <reified I : Item, Item> Stack<Item>.popUntil(): Boolean =
    popUntil { item -> item is I }

public enum class StackEvent {
    Push,
    Replace,
    Pop,
    Idle
}

public interface Stack<Item> {

    public val items: List<Item>

    public val lastEvent: StackEvent

    public val lastItemOrNull: Item?

    public val size: Int

    public val isEmpty: Boolean

    public val canPop: Boolean

    public infix fun push(item: Item)

    public infix fun push(items: List<Item>)

    public infix fun replace(item: Item)

    public infix fun replaceAll(item: Item)

    public infix fun replaceAll(items: List<Item>)

    public fun pop(): Boolean

    public fun popAll()

    public infix fun popUntil(predicate: (Item) -> Boolean): Boolean

    public operator fun plusAssign(item: Item)

    public operator fun plusAssign(items: List<Item>)

    public fun clearEvent()
}
