package cafe.adriel.voyager.core.concurrent

public actual class AtomicInt32 actual constructor(initialValue: Int) {
    private val delegate = kotlin.native.concurrent.AtomicInt(initialValue)
    public actual fun getAndIncrement(): Int {
        return delegate.addAndGet(1) // FIXME: should use increment() ?
    }
}
