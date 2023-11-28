package cafe.adriel.voyager.core.concurrent

import kotlinx.atomicfu.locks.SynchronizedObject

public actual class ThreadSafeSet<T>(
    syncObject: SynchronizedObject,
    delegate: MutableSet<T>
) : MutableSet<T>, ThreadSafeMutableCollection<T>(syncObject, delegate) {
    public actual constructor() : this(delegate = mutableSetOf())
    public constructor(delegate: MutableSet<T>) : this(SynchronizedObject(), delegate)
}
