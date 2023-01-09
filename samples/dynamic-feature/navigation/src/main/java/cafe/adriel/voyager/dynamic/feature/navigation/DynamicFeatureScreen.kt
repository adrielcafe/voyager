package cafe.adriel.voyager.dynamic.feature.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.google.android.play.core.splitinstall.model.SplitInstallSessionStatus
import kotlin.reflect.full.createInstance

/**
 * Google Play best practices recommend show status to use while
 * downloading a dynamic feature
 */
public typealias CustomDynamicScreenContent = @Composable (
    state: DynamicFeatureInstallState,
    installedModules: Set<String>,
    requestUserConfirmation: (requestCode: Int) -> Unit,
    retry: () -> Unit
) -> Unit

class DynamicFeatureScreen(
    private val screenProvider: DynamicFeatureScreenProvider,
    private val content: CustomDynamicScreenContent,
) : Screen {
    internal var currentProvider: DynamicFeatureProvider? = null

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val provider = safeProvider()
        val state by provider.state.collectAsState()
        var tryInstall by remember {
            mutableStateOf(true)
        }

        SideEffect {
            if (tryInstall || state.status == SplitInstallSessionStatus.INSTALLED) {
                tryInstallOrNavigate(navigator)
                tryInstall = false
            }
        }

        content(
            state = state,
            installedModules = provider.installedModules,
            requestUserConfirmation = provider::requestUserConfirmation,
            retry = {
                tryInstall = true
            },
        )
    }

    private fun tryInstallOrNavigate(navigator: Navigator) {
        val provider = safeProvider()
        if (!provider.hasInstallAlready(screenProvider.moduleName)) {
            provider.install(screenProvider)
        } else {
            val forName = Class.forName(screenProvider.entryPointer)
            val entryPointerInstance = forName.kotlin.createInstance()
            if (entryPointerInstance is Screen) {
                navigator.replace(entryPointerInstance)
            } else {
                error("Your dynamic feature entry pointer must be a ${Screen::class.qualifiedName}")
            }
        }
    }

    private fun safeProvider(): DynamicFeatureProvider = currentProvider
        ?: error("No ${DynamicFeatureProvider::class.qualifiedName} found in the navigation scope")
}
