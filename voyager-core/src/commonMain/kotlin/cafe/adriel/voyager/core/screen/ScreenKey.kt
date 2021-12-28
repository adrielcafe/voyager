package cafe.adriel.voyager.core.screen

import cafe.adriel.voyager.core.concurrent.AtomicInt32

public typealias ScreenKey = String

private val nextScreenKey = AtomicInt32(0)

public val Screen.uniqueScreenKey: ScreenKey
    get() = "Screen#${nextScreenKey.getAndIncrement()}"
