package cafe.adriel.voyager.core.screen

import java.util.concurrent.atomic.AtomicInteger

public typealias ScreenKey = String

private val nextScreenKey = AtomicInteger(0)

public val Screen.uniqueScreenKey: ScreenKey
    get() = "Screen#${nextScreenKey.getAndIncrement()}"
