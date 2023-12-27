package cafe.adriel.voyager.core.concurrent

import kotlinx.atomicfu.locks.SynchronizedObject
import kotlinx.atomicfu.locks.synchronized

public actual class ThreadSafeList<T>(
    private val delegate: MutableList<T>
) : MutableList<T> {
    public actual constructor() : this(delegate = mutableListOf())
    private val syncObject = SynchronizedObject()

    override val size: Int
        get() = delegate.size

    override fun contains(element: T): Boolean {
        return synchronized(syncObject) { delegate.contains(element) }
    }

    override fun containsAll(elements: Collection<T>): Boolean {
        return synchronized(syncObject) { delegate.containsAll(elements) }
    }

    override fun get(index: Int): T {
        return synchronized(syncObject) { delegate.get(index) }
    }

    override fun indexOf(element: T): Int {
        return synchronized(syncObject) { delegate.indexOf(element) }
    }

    override fun isEmpty(): Boolean {
        return synchronized(syncObject) { delegate.isEmpty() }
    }

    override fun iterator(): MutableIterator<T> {
        return synchronized(syncObject) { delegate.iterator() }
    }

    override fun lastIndexOf(element: T): Int {
        return synchronized(syncObject) { delegate.lastIndexOf(element) }
    }

    override fun add(element: T): Boolean {
        return synchronized(syncObject) { delegate.add(element) }
    }

    override fun add(index: Int, element: T) {
        return synchronized(syncObject) { delegate.add(index, element) }
    }

    override fun addAll(index: Int, elements: Collection<T>): Boolean {
        return synchronized(syncObject) { delegate.addAll(index, elements) }
    }

    override fun addAll(elements: Collection<T>): Boolean {
        return synchronized(syncObject) { delegate.addAll(elements) }
    }

    override fun clear() {
        return synchronized(syncObject) { delegate.clear() }
    }

    override fun listIterator(): MutableListIterator<T> {
        return synchronized(syncObject) { delegate.listIterator() }
    }

    override fun listIterator(index: Int): MutableListIterator<T> {
        return synchronized(syncObject) { delegate.listIterator(index) }
    }

    override fun remove(element: T): Boolean {
        return synchronized(syncObject) { delegate.remove(element) }
    }

    override fun removeAll(elements: Collection<T>): Boolean {
        return synchronized(syncObject) { delegate.removeAll(elements) }
    }

    override fun removeAt(index: Int): T {
        return synchronized(syncObject) { delegate.removeAt(index) }
    }

    override fun retainAll(elements: Collection<T>): Boolean {
        return synchronized(syncObject) { delegate.retainAll(elements) }
    }

    override fun set(index: Int, element: T): T {
        return synchronized(syncObject) { delegate.set(index, element) }
    }

    override fun subList(fromIndex: Int, toIndex: Int): MutableList<T> {
        return synchronized(syncObject) { delegate.subList(fromIndex, toIndex) }
    }
}
