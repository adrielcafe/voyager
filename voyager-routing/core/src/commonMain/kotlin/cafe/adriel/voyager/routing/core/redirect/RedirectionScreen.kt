package cafe.adriel.voyager.routing.core.redirect

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.routing.core.application.ApplicationCall

internal data class RedirectionScreen(
    val call: ApplicationCall,
    val name: String = "",
    val path: String = "",
) : Screen {

    @Composable
    override fun Content() {
        // No-op because this screen is used to do a redirection only
    }
}
