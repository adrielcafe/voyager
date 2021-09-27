package cafe.adriel.voyager.sample.hiltIntegration

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import cafe.adriel.voyager.androidx.AndroidScreen
import cafe.adriel.voyager.hilt.getViewModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.sample.DetailsContent

data class HiltDetailsScreen(
    val index: Int
) : AndroidScreen() {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel: HiltDetailsViewModel = getViewModel(
            viewModelProviderFactory = provideFactory(index)
        )

        DetailsContent(viewModel, "Item #${viewModel.index}", navigator::pop)
    }

    private fun provideFactory(
        index: Int
    ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return HiltDetailsViewModel(index) as T
        }
    }
}
