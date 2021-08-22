package cafe.adriel.voyager.core.screen

import java.util.concurrent.atomic.AtomicInteger

private val nextScreenKey = AtomicInteger(0)

public val Screen.uniqueScreenKey: String
    get() = "Screen#${nextScreenKey.getAndIncrement()}"
