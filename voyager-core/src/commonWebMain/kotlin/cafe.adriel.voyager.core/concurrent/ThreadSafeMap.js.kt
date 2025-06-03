package cafe.adriel.voyager.core.concurrent

import kotlinx.atomicfu.locks.SynchronizedObject
import kotlinx.atomicfu.locks.synchronized

public actual class ThreadSafeMap<K, V>(
    private val delegate: MutableMap<K, V>
) : MutableMap<K, V> {
    public actual constructor() : this(delegate = mutableMapOf())
    private val syncObject = SynchronizedObject()

    actual override val size: Int
        get() = synchronized(syncObject) { delegate.size }

    actual override fun containsKey(key: K): Boolean {
        return synchronized(syncObject) { delegate.containsKey(key) }
    }

    actual override fun containsValue(value: V): Boolean {
        return synchronized(syncObject) { delegate.containsValue(value) }
    }

    actual override fun get(key: K): V? {
        return synchronized(syncObject) { delegate[key] }
    }

    actual override fun isEmpty(): Boolean {
        return synchronized(syncObject) { delegate.isEmpty() }
    }

    actual override val entries: MutableSet<MutableMap.MutableEntry<K, V>>
        get() = delegate.entries
    actual override val keys: MutableSet<K>
        get() = delegate.keys
    actual override val values: MutableCollection<V>
        get() = delegate.values

    actual override fun clear() {
        synchronized(syncObject) { delegate.clear() }
    }

    actual override fun put(key: K, value: V): V? {
        return synchronized(syncObject) { delegate.put(key, value) }
    }

    actual override fun putAll(from: Map<out K, V>) {
        synchronized(syncObject) { delegate.putAll(from) }
    }

    actual override fun remove(key: K): V? {
        return synchronized(syncObject) { delegate.remove(key) }
    }
}
