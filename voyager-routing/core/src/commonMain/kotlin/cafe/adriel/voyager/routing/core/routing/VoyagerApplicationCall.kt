package cafe.adriel.voyager.routing.core.routing

import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.routing.core.application.Application
import cafe.adriel.voyager.routing.core.application.ApplicationCall
import io.ktor.http.Parameters
import io.ktor.util.Attributes
import kotlinx.coroutines.CoroutineScope
import kotlin.coroutines.CoroutineContext

public data class VoyagerApplicationCall(
    override val application: Application,
    override val uri: String = "",
    override val attributes: Attributes = Attributes(),
    override val parameters: Parameters = Parameters.Empty,
    public val name: String = "",
    public val popUntilPredicate: ((Screen) -> Boolean)? = null,
    public val pathReplacements: Parameters = Parameters.Empty,
    public val replaceAll: Boolean = false,
    public val replaceCurrent: Boolean = false,
    public val redirectAttempt: Int = 0,
    private val route: VoyagerRoute,
) : ApplicationCall, CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = application.coroutineContext

    init {
        check(uri.isBlank() || name.isBlank()) {
            "Both uri and name are not allowed"
        }
    }

    val isPop: Boolean
        get() = popUntilPredicate != null || (uri.isBlank() && name.isBlank())

    val routing: VoyagerRouting
        get() = route.routing()
}
