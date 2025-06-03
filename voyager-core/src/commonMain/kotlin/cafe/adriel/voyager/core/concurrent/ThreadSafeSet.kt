package cafe.adriel.voyager.core.concurrent

public expect class ThreadSafeSet<T>() : MutableSet<T> {
    override fun add(element: T): Boolean
    override fun addAll(elements: Collection<T>): Boolean
    override fun clear()
    override fun iterator(): MutableIterator<T>
    override fun remove(element: T): Boolean
    override fun removeAll(elements: Collection<T>): Boolean
    override fun retainAll(elements: Collection<T>): Boolean
    override fun contains(element: T): Boolean
    override fun containsAll(elements: Collection<T>): Boolean
    override fun isEmpty(): Boolean
    override val size: Int
}
