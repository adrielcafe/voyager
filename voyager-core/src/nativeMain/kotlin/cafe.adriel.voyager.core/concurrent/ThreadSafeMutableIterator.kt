package cafe.adriel.voyager.core.concurrent

import kotlinx.atomicfu.locks.SynchronizedObject
import kotlinx.atomicfu.locks.synchronized

internal open class ThreadSafeMutableIterator<E>(
    private val syncObject: SynchronizedObject,
    private val delegate: MutableIterator<E>
) : MutableIterator<E> {
    override fun hasNext(): Boolean = synchronized(syncObject) { delegate.hasNext() }

    override fun next(): E = synchronized(syncObject) { delegate.next() }

    override fun remove() {
        synchronized(syncObject) { delegate.remove() }
    }
}

internal class ThreadSafeMutableListIterator<E>(
    private val syncObject: SynchronizedObject,
    private val delegate: MutableListIterator<E>
) : ThreadSafeMutableIterator<E>(syncObject, delegate),
    MutableListIterator<E> {

    override fun hasPrevious(): Boolean = synchronized(syncObject) { delegate.hasPrevious() }

    override fun nextIndex(): Int = synchronized(syncObject) { delegate.nextIndex() }

    override fun previous(): E = synchronized(syncObject) { delegate.previous() }

    override fun previousIndex(): Int = synchronized(syncObject) { delegate.previousIndex() }

    override fun add(element: E) {
        synchronized(syncObject) { delegate.add(element) }
    }

    override fun set(element: E) {
        synchronized(syncObject) { delegate.set(element) }
    }
}
