package cafe.adriel.voyager.core.concurrent

import kotlinx.atomicfu.locks.SynchronizedObject
import kotlinx.atomicfu.locks.synchronized

public open class ThreadSafeMutableCollection<T>internal constructor(
    private val syncObject: SynchronizedObject,
    private val delegate: MutableCollection<T>
) : MutableCollection<T> {

    override val size: Int
        get() = synchronized(syncObject) { delegate.size }

    override fun contains(element: T): Boolean {
        return synchronized(syncObject) { delegate.contains(element) }
    }

    override fun containsAll(elements: Collection<T>): Boolean {
        return synchronized(syncObject) { delegate.containsAll(elements) }
    }

    override fun isEmpty(): Boolean {
        return synchronized(syncObject) { delegate.isEmpty() }
    }

    override fun iterator(): MutableIterator<T> {
        return synchronized(syncObject) { ThreadSafeMutableIterator(syncObject, delegate.iterator()) }
    }

    override fun add(element: T): Boolean {
        return synchronized(syncObject) { delegate.add(element) }
    }

    override fun addAll(elements: Collection<T>): Boolean {
        return synchronized(syncObject) { delegate.addAll(elements) }
    }

    override fun clear() {
        return synchronized(syncObject) { delegate.clear() }
    }

    override fun remove(element: T): Boolean {
        return synchronized(syncObject) { delegate.remove(element) }
    }

    override fun removeAll(elements: Collection<T>): Boolean {
        return synchronized(syncObject) { delegate.removeAll(elements) }
    }

    override fun retainAll(elements: Collection<T>): Boolean {
        return synchronized(syncObject) { delegate.retainAll(elements) }
    }
}
