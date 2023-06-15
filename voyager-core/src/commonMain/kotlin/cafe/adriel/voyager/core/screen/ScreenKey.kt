package cafe.adriel.voyager.core.screen

public typealias ScreenKey = String

internal expect fun randomUuid(): String

public val Screen.uniqueScreenKey: ScreenKey
    get() = "Screen#${randomUuid()}"
