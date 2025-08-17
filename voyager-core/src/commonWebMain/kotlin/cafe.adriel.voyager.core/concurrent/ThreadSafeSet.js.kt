package cafe.adriel.voyager.core.concurrent

import kotlinx.atomicfu.locks.SynchronizedObject
import kotlinx.atomicfu.locks.synchronized

public actual class ThreadSafeSet<T>(
    private val delegate: MutableSet<T>
) : MutableSet<T> {
    public actual constructor() : this(delegate = mutableSetOf())
    private val syncObject = SynchronizedObject()

    actual override val size: Int
        get() = delegate.size

    actual override fun contains(element: T): Boolean {
        return synchronized(syncObject) { delegate.contains(element) }
    }

    actual override fun containsAll(elements: Collection<T>): Boolean {
        return synchronized(syncObject) { delegate.containsAll(elements) }
    }

    actual override fun isEmpty(): Boolean {
        return synchronized(syncObject) { delegate.isEmpty() }
    }

    actual override fun iterator(): MutableIterator<T> {
        return synchronized(syncObject) { delegate.iterator() }
    }

    actual override fun add(element: T): Boolean {
        return synchronized(syncObject) { delegate.add(element) }
    }

    actual override fun addAll(elements: Collection<T>): Boolean {
        return synchronized(syncObject) { delegate.addAll(elements) }
    }

    actual override fun clear() {
        return synchronized(syncObject) { delegate.clear() }
    }

    actual override fun remove(element: T): Boolean {
        return synchronized(syncObject) { delegate.remove(element) }
    }

    actual override fun removeAll(elements: Collection<T>): Boolean {
        return synchronized(syncObject) { delegate.removeAll(elements) }
    }

    actual override fun retainAll(elements: Collection<T>): Boolean {
        return synchronized(syncObject) { delegate.retainAll(elements) }
    }
}
