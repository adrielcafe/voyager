package cafe.adriel.voyager.sample.androidViewModel

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.androidx.AndroidScreen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.sample.ListContent
import org.koin.androidx.compose.getStateViewModel

class AndroidListScreen : AndroidScreen() {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = getStateViewModel<AndroidListViewModel>()

        ListContent(viewModel.items, onClick = { index -> navigator.push(AndroidDetailsScreen(index)) })
    }
}
