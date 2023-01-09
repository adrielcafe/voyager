package cafe.adriel.voyager.dynamic.feature.navigation

/**
 * Google Play Wrapper [com.google.android.play.core.splitinstall.SplitInstallSessionState]
 */
data class DynamicFeatureInstallState(
    val status: Int = -1,
    val bytesDownloaded: Long = 0,
    val totalBytesToDownload: Long = 0,
    val errorCode: Int = -1,
    val sessionId: Int = Int.MIN_VALUE,
    val hasTerminalStatus: Boolean = false,
    val languages: List<String> = emptyList(),
    val moduleNames: List<String> = emptyList(),
)
