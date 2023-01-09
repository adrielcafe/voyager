package cafe.adriel.voyager.dynamic.feature.navigation

import cafe.adriel.voyager.core.registry.ScreenProvider

/**
 * As a [ScreenProvider] we can use [cafe.adriel.voyager.core.registry.ScreenRegistry] to
 * register screens by type without directly use [DynamicFeatureScreen]
 */
interface DynamicFeatureScreenProvider : ScreenProvider {
    val entryPointer: String
    val moduleName: String
}
