package cafe.adriel.voyager.sample.hiltIntegration

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.androidx.AndroidScreen
import cafe.adriel.voyager.hilt.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.sample.ListContent

class HiltListScreen : AndroidScreen() {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        // Uncomment version below if you want keep using ViewModel instead of to convert it to ScreenModel
        // val viewModel: HiltListViewModel = getViewModel()
        val viewModel: HiltListScreenModel = getScreenModel()

        ListContent(viewModel.items, onClick = { index -> navigator.push(HiltDetailsScreen(index)) })
    }
}
