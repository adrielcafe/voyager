package cafe.adriel.voyager.dynamic.feature.navigation

import android.app.Activity
import com.google.android.play.core.splitinstall.SplitInstallManagerFactory
import com.google.android.play.core.splitinstall.SplitInstallRequest
import com.google.android.play.core.splitinstall.SplitInstallSessionState
import com.google.android.play.core.splitinstall.SplitInstallStateUpdatedListener
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

internal class DynamicFeatureManager(
    private val activity: Activity
) : DynamicFeatureProvider, SplitInstallStateUpdatedListener {
    // Creates an instance of SplitInstallManager.
    private val splitInstallManager = SplitInstallManagerFactory.create(activity)
    private val mutableState = MutableStateFlow(DynamicFeatureInstallState())
    private var currentState: SplitInstallSessionState? = null

    override val installedModules: Set<String>
        get() = splitInstallManager.installedModules

    override val state: StateFlow<DynamicFeatureInstallState>
        get() = mutableState

    override fun onStateUpdate(state: SplitInstallSessionState) {
        currentState = state
        mutableState.value = DynamicFeatureInstallState(
            status = state.status(),
            bytesDownloaded = state.bytesDownloaded(),
            totalBytesToDownload = state.totalBytesToDownload(),
            errorCode = state.errorCode(),
            sessionId = state.sessionId(),
            hasTerminalStatus = state.hasTerminalStatus(),
            languages = state.languages(),
            moduleNames = state.moduleNames(),
        )
    }

    override fun requestUserConfirmation(requestCode: Int) {
        val valid = currentState ?: return
        splitInstallManager.startConfirmationDialogForResult(valid, activity, requestCode)
    }

    override fun install(vararg providers: DynamicFeatureScreenProvider) {
        val request = SplitInstallRequest.newBuilder()
        for (provider in providers) {
            request.addModule(provider.moduleName)
        }
        splitInstallManager.startInstall(request.build())
    }

    override fun hasInstallAlready(moduleName: String): Boolean {
        return splitInstallManager.installedModules.contains(moduleName)
    }

    fun register() {
        // Registers the listener.
        splitInstallManager.registerListener(this)
    }

    fun unregister() {
        // Unregisters the listener.
        splitInstallManager.unregisterListener(this)
    }
}
