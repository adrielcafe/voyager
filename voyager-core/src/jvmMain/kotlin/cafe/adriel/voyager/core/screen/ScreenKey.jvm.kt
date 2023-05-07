package cafe.adriel.voyager.core.screen

import java.util.UUID

internal actual fun randomUuid(): String = UUID.randomUUID().toString()
