package cafe.adriel.voyager.core.lifecycle

public actual class SavedState {
    public actual fun putBundle(key: String?, savedState: SavedState?) {}

    public actual fun putAll(savedState: SavedState) {}

    public actual fun getBundle(key: String?): SavedState? = null

    public actual fun remove(key: String?) {}
}
