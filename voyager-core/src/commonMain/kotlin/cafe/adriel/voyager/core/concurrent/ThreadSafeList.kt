package cafe.adriel.voyager.core.concurrent

public expect class ThreadSafeList<T>() : MutableList<T> {
    override fun add(element: T): Boolean
    override fun add(index: Int, element: T)
    override fun addAll(elements: Collection<T>): Boolean
    override fun addAll(index: Int, elements: Collection<T>): Boolean
    override fun clear()
    override fun listIterator(): MutableListIterator<T>
    override fun listIterator(index: Int): MutableListIterator<T>
    override fun remove(element: T): Boolean
    override fun removeAll(elements: Collection<T>): Boolean
    override fun removeAt(index: Int): T
    override fun retainAll(elements: Collection<T>): Boolean
    override fun set(index: Int, element: T): T
    override fun subList(fromIndex: Int, toIndex: Int): MutableList<T>
    override fun contains(element: T): Boolean
    override fun containsAll(elements: Collection<T>): Boolean
    override fun get(index: Int): T
    override fun indexOf(element: T): Int
    override fun isEmpty(): Boolean
    override fun iterator(): MutableIterator<T>
    override fun lastIndexOf(element: T): Int
    override val size: Int
}
