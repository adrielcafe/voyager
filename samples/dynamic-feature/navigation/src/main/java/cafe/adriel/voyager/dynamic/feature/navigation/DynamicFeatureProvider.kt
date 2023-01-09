package cafe.adriel.voyager.dynamic.feature.navigation

import kotlinx.coroutines.flow.StateFlow

interface DynamicFeatureProvider {
    val installedModules: Set<String>

    val state: StateFlow<DynamicFeatureInstallState>

    fun hasInstallAlready(moduleName: String): Boolean

    fun install(vararg providers: DynamicFeatureScreenProvider)

    fun requestUserConfirmation(requestCode: Int)
}
