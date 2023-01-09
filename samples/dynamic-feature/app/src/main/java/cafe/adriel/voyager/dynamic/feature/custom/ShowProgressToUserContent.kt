package cafe.adriel.voyager.dynamic.feature.custom

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.dynamic.feature.navigation.DynamicFeatureInstallState
import cafe.adriel.voyager.dynamic.feature.navigation.DynamicFeatureScreen
import cafe.adriel.voyager.dynamic.feature.navigation.DynamicFeatureScreenProvider
import com.google.android.play.core.splitinstall.model.SplitInstallSessionStatus

fun dynamicScreen(screenProvider: DynamicFeatureScreenProvider): DynamicFeatureScreen =
    DynamicFeatureScreen(
        screenProvider = screenProvider,
        content = { state, installedModules, requestUserConfirmation, retry ->
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                ShowProgressToUserContent(
                    state = state,
                    installedModules = installedModules,
                    requestUserConfirmation = requestUserConfirmation,
                    retry = retry
                )
            }
        }
    )

@Composable
internal fun ShowProgressToUserContent(
    state: DynamicFeatureInstallState,
    installedModules: Set<String>,
    requestUserConfirmation: (requestCode: Int) -> Unit,
    retry: () -> Unit,
) {
    val multiInstall = state.moduleNames.size > 1
    val names = state.moduleNames.joinToString(" - ")

    when (state.status) {
        SplitInstallSessionStatus.DOWNLOADING -> {
            LoadingComponent(
                bytesDownloaded = state.bytesDownloaded,
                totalBytesToDownload = state.totalBytesToDownload,
                message = "Downloading $names",
            )
        }

        SplitInstallSessionStatus.REQUIRES_USER_CONFIRMATION -> {
            Text(
                text = "We need user confirmation to download",
                fontSize = 24.sp,
                modifier = Modifier
                    .background(color = Color.Cyan)
                    .clickable {
                        requestUserConfirmation(1234)
                    }
            )
        }

        SplitInstallSessionStatus.INSTALLED -> {
            Text(
                text = "Success installation of [$names].\nNavigation will happen automatically :D",
                fontSize = 24.sp,
            )
        }

        SplitInstallSessionStatus.INSTALLING -> {
            LoadingComponent(
                bytesDownloaded = state.bytesDownloaded,
                totalBytesToDownload = state.totalBytesToDownload,
                message = "Installing $names",
            )
        }

        SplitInstallSessionStatus.FAILED -> {
            Text(
                text = "Error: ${state.errorCode} for module ${state.moduleNames}",
                fontSize = 24.sp,
                modifier = Modifier
                    .background(color = Color.Cyan)
                    .clickable {
                        retry.invoke()
                    }
            )
        }
    }
}

@Composable
internal fun LoadingComponent(
    bytesDownloaded: Long,
    totalBytesToDownload: Long,
    message: String
) {
    Column {
        LinearProgressIndicator(
            progress = bytesDownloaded / totalBytesToDownload.toFloat()
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = message,
            fontSize = 24.sp,
        )
    }
}
