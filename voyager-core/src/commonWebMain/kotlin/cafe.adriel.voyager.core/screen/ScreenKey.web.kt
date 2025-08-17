package cafe.adriel.voyager.core.screen

import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
internal actual fun randomUuid(): String = Uuid.random().toString()
