package cafe.adriel.voyager.core.hook

public inline fun <reified T> Hookable<T>.addHooks(vararg hooks: T) {
    addHooks(hooks.toList())
}

public inline fun <reified T> Hookable<T>.removeHooks(vararg hooks: T) {
    removeHooks(hooks.toList())
}

public interface Hookable<T> {

    public val hooks: List<T>

    public fun addHooks(hooks: List<T>)

    public fun removeHooks(hooks: List<T>)

    public fun clearHooks()
}
