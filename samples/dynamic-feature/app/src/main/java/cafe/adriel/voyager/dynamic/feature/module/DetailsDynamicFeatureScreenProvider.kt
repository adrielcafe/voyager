package cafe.adriel.voyager.dynamic.feature.module

import cafe.adriel.voyager.dynamic.feature.navigation.DynamicFeatureScreenProvider

object DetailsDynamicFeatureScreenProvider : DynamicFeatureScreenProvider {
    override val moduleName = "details"
    override val entryPointer = "cafe.adriel.voyager.dynamic.feature.details.DetailsScreen"
}
