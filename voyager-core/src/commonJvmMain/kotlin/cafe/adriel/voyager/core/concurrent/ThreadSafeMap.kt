package cafe.adriel.voyager.core.concurrent

import java.util.concurrent.ConcurrentHashMap

public actual class ThreadSafeMap<K, V> : MutableMap<K, V> by ConcurrentHashMap()
