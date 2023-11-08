package cafe.adriel.voyager.core.concurrent

import kotlinx.atomicfu.locks.SynchronizedObject
import kotlinx.atomicfu.locks.synchronized

public actual class ThreadSafeList<T> internal constructor(
    private val syncObject: SynchronizedObject,
    private val delegate: MutableList<T>
) : MutableList<T>, ThreadSafeMutableCollection<T>(syncObject, delegate) {
    public actual constructor() : this(delegate = mutableListOf())
    public constructor(delegate: MutableList<T>) : this(SynchronizedObject(), delegate)

    override fun get(index: Int): T {
        return synchronized(syncObject) { delegate.get(index) }
    }

    override fun indexOf(element: T): Int {
        return synchronized(syncObject) { delegate.indexOf(element) }
    }

    override fun lastIndexOf(element: T): Int {
        return synchronized(syncObject) { delegate.lastIndexOf(element) }
    }

    override fun add(index: Int, element: T) {
        return synchronized(syncObject) { delegate.add(index, element) }
    }

    override fun addAll(index: Int, elements: Collection<T>): Boolean {
        return synchronized(syncObject) { delegate.addAll(index, elements) }
    }

    override fun listIterator(): MutableListIterator<T> {
        return synchronized(syncObject) { ThreadSafeMutableListIterator(syncObject, delegate.listIterator()) }
    }

    override fun listIterator(index: Int): MutableListIterator<T> {
        return synchronized(syncObject) { ThreadSafeMutableListIterator(syncObject, delegate.listIterator(index)) }
    }

    override fun removeAt(index: Int): T {
        return synchronized(syncObject) { delegate.removeAt(index) }
    }

    override fun set(index: Int, element: T): T {
        return synchronized(syncObject) { delegate.set(index, element) }
    }

    override fun subList(fromIndex: Int, toIndex: Int): MutableList<T> {
        return synchronized(syncObject) { ThreadSafeList(syncObject, delegate.subList(fromIndex, toIndex)) }
    }
}
