package cafe.adriel.voyager.sample.screenModel

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.currentCompositeKeyHash
import androidx.compose.runtime.getValue
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import cafe.adriel.voyager.core.screen.uniqueScreenKey
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.sample.DetailsContent
import cafe.adriel.voyager.sample.LoadingContent

data class DetailsScreen(
    val index: Int
) : Screen {

    override val key: ScreenKey = uniqueScreenKey

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val screenModel = rememberScreenModel { DetailsScreenModel(index) }
        val state by screenModel.state.collectAsState()

        when (val result = state) {
            is DetailsScreenModel.State.Loading -> LoadingContent()
            is DetailsScreenModel.State.Result -> DetailsContent(screenModel, result.item, navigator::pop)
        }

        LaunchedEffect(currentCompositeKeyHash) {
            screenModel.getItem(index)
        }
    }
}
