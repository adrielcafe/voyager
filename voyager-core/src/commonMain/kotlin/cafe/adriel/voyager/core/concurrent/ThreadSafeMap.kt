package cafe.adriel.voyager.core.concurrent

public expect class ThreadSafeMap<K, V>() : MutableMap<K, V> {
    override fun clear()
    override fun put(key: K, value: V): V?
    override fun putAll(from: Map<out K, V>)
    override fun remove(key: K): V?
    override val entries: MutableSet<MutableMap.MutableEntry<K, V>>
    override val keys: MutableSet<K>
    override val values: MutableCollection<V>
    override fun containsKey(key: K): Boolean
    override fun containsValue(value: V): Boolean
    override fun get(key: K): V?
    override fun isEmpty(): Boolean
    override val size: Int
}
