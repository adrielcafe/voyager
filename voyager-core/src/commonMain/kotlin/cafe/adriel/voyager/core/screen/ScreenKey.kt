package cafe.adriel.voyager.core.screen

import com.benasher44.uuid.uuid4

public typealias ScreenKey = String

public val Screen.uniqueScreenKey: ScreenKey
    get() = "Screen#${uuid4()}"
