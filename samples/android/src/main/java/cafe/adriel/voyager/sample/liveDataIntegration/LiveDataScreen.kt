package cafe.adriel.voyager.sample.liveDataIntegration

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.currentCompositeKeyHash
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import cafe.adriel.voyager.core.screen.uniqueScreenKey
import cafe.adriel.voyager.sample.ListContent
import cafe.adriel.voyager.sample.LoadingContent

class LiveDataScreen : Screen {

    override val key: ScreenKey = uniqueScreenKey

    @Composable
    override fun Content() {
        val screenModel = rememberScreenModel { LiveDataScreenModel() }
        val state by screenModel.state.observeAsState()

        when (val result = state) {
            is LiveDataScreenModel.State.Loading -> LoadingContent()
            is LiveDataScreenModel.State.Result -> ListContent(result.items)
            else -> Unit
        }

        LaunchedEffect(currentCompositeKeyHash) {
            screenModel.getItems()
        }
    }
}
