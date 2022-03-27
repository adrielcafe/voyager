package cafe.adriel.voyager.core.concurrent

// FIXME
public actual class ThreadSafeMap<K, V>(
    private val delegate: MutableMap<K, V>
) : MutableMap<K, V> by delegate {
    public actual constructor() : this(delegate = mutableMapOf())
}
