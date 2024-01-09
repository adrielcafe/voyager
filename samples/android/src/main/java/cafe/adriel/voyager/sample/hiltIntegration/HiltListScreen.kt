package cafe.adriel.voyager.sample.hiltIntegration

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.hilt.hiltViewModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.sample.ListContent

class HiltListScreen : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel: HiltListViewModel = hiltViewModel()

        // Uncomment version below if you want to use ScreenModel
        // val viewModel: HiltListScreenModel = getScreenModel()

        ListContent(viewModel.items, onClick = { index -> navigator.push(HiltDetailsScreen(index)) })
    }
}
