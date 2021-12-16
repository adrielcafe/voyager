package cafe.adriel.voyager.core.registry

private typealias ScreenModule = ScreenRegistry.() -> Unit

public fun screenModule(block: ScreenModule): ScreenModule =
    { block() }
