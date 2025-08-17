package cafe.adriel.voyager.core.concurrent

import kotlinx.atomicfu.locks.SynchronizedObject
import kotlinx.atomicfu.locks.synchronized

public actual class ThreadSafeList<T>(
    private val delegate: MutableList<T>
) : MutableList<T> {
    public actual constructor() : this(delegate = mutableListOf())
    private val syncObject = SynchronizedObject()

    actual override val size: Int
        get() = delegate.size

    actual override fun contains(element: T): Boolean {
        return synchronized(syncObject) { delegate.contains(element) }
    }

    actual override fun containsAll(elements: Collection<T>): Boolean {
        return synchronized(syncObject) { delegate.containsAll(elements) }
    }

    actual override fun get(index: Int): T {
        return synchronized(syncObject) { delegate.get(index) }
    }

    actual override fun indexOf(element: T): Int {
        return synchronized(syncObject) { delegate.indexOf(element) }
    }

    actual override fun isEmpty(): Boolean {
        return synchronized(syncObject) { delegate.isEmpty() }
    }

    actual override fun iterator(): MutableIterator<T> {
        return synchronized(syncObject) { delegate.iterator() }
    }

    actual override fun lastIndexOf(element: T): Int {
        return synchronized(syncObject) { delegate.lastIndexOf(element) }
    }

    actual override fun add(element: T): Boolean {
        return synchronized(syncObject) { delegate.add(element) }
    }

    actual override fun add(index: Int, element: T) {
        return synchronized(syncObject) { delegate.add(index, element) }
    }

    actual override fun addAll(index: Int, elements: Collection<T>): Boolean {
        return synchronized(syncObject) { delegate.addAll(index, elements) }
    }

    actual override fun addAll(elements: Collection<T>): Boolean {
        return synchronized(syncObject) { delegate.addAll(elements) }
    }

    actual override fun clear() {
        return synchronized(syncObject) { delegate.clear() }
    }

    actual override fun listIterator(): MutableListIterator<T> {
        return synchronized(syncObject) { delegate.listIterator() }
    }

    actual override fun listIterator(index: Int): MutableListIterator<T> {
        return synchronized(syncObject) { delegate.listIterator(index) }
    }

    actual override fun remove(element: T): Boolean {
        return synchronized(syncObject) { delegate.remove(element) }
    }

    actual override fun removeAll(elements: Collection<T>): Boolean {
        return synchronized(syncObject) { delegate.removeAll(elements) }
    }

    actual override fun removeAt(index: Int): T {
        return synchronized(syncObject) { delegate.removeAt(index) }
    }

    actual override fun retainAll(elements: Collection<T>): Boolean {
        return synchronized(syncObject) { delegate.retainAll(elements) }
    }

    actual override fun set(index: Int, element: T): T {
        return synchronized(syncObject) { delegate.set(index, element) }
    }

    actual override fun subList(fromIndex: Int, toIndex: Int): MutableList<T> {
        return synchronized(syncObject) { delegate.subList(fromIndex, toIndex) }
    }
}
