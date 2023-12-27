package cafe.adriel.voyager.core.concurrent

import kotlinx.atomicfu.atomic

public actual class AtomicInt32 actual constructor(initialValue: Int) {
    private val delegate = atomic(initialValue)
    public actual fun getAndIncrement(): Int {
        return delegate.incrementAndGet()
    }
}
