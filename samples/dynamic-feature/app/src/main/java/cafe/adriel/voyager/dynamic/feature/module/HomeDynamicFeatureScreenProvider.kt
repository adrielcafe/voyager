package cafe.adriel.voyager.dynamic.feature.module

import cafe.adriel.voyager.dynamic.feature.navigation.DynamicFeatureScreenProvider

object HomeDynamicFeatureScreenProvider : DynamicFeatureScreenProvider {
    override val moduleName = "home"
    override val entryPointer = "cafe.adriel.voyager.dynamic.feature.home.HomeScreen"
}
