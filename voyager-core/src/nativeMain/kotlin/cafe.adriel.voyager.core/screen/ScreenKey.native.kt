package cafe.adriel.voyager.core.screen

import platform.Foundation.NSUUID

internal actual fun randomUuid(): String = NSUUID().UUIDString()
