package cafe.adriel.voyager.core.concurrent

public expect class AtomicInt32(initialValue: Int) {
    public fun getAndIncrement(): Int
}
