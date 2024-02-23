package cafe.adriel.voyager.core.screen

import com.benasher44.uuid.uuid4

internal actual fun randomUuid(): String = uuid4().toString()
