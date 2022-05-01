package cafe.adriel.voyager.core.concurrent

import java.util.concurrent.CopyOnWriteArrayList

public actual class ThreadSafeList<T> : MutableList<T> by CopyOnWriteArrayList()
