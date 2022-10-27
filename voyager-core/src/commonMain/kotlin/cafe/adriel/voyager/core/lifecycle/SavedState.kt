package cafe.adriel.voyager.core.lifecycle

public expect class SavedState() {
    public fun putBundle(key: String?, savedState: SavedState?)

    public fun putAll(savedState: SavedState)

    public fun getBundle(key: String?): SavedState?

    public fun remove(key: String?)
}
