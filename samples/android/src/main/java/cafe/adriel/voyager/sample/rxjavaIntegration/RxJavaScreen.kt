package cafe.adriel.voyager.sample.rxJavaIntegration

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.currentCompositeKeyHash
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rxjava3.subscribeAsState
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import cafe.adriel.voyager.core.screen.uniqueScreenKey
import cafe.adriel.voyager.sample.ListContent
import cafe.adriel.voyager.sample.LoadingContent

class RxJavaScreen : Screen {

    override val key: ScreenKey = uniqueScreenKey

    @Composable
    override fun Content() {
        val screenModel = rememberScreenModel { RxJavaScreenModel() }
        val state by screenModel.state.subscribeAsState(initial = RxJavaScreenModel.State.Loading)

        when (val result = state) {
            is RxJavaScreenModel.State.Loading -> LoadingContent()
            is RxJavaScreenModel.State.Result -> ListContent(result.items)
        }

        LaunchedEffect(currentCompositeKeyHash) {
            screenModel.getItems()
        }
    }
}
