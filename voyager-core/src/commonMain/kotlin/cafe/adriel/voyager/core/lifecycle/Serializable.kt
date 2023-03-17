package cafe.adriel.voyager.core.lifecycle

/**
 * Multiplatform reference to Java `Serializable` interface.
 *
 * Any object that will be pass to a Screen as parameter, on Android target, requires to be serializable in a
 * Bundle, so if your project is multiplatform and targeting Android, to prevent State Restoration issues on Android
 * the easiest way is to apply this interface to your Models.
 */
public expect interface JavaSerializable
