package cafe.adriel.voyager.sample.hiltIntegration

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import cafe.adriel.voyager.core.screen.uniqueScreenKey
import cafe.adriel.voyager.hilt.hiltViewModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.sample.DetailsContent

data class HiltDetailsScreen(
    val index: Int
) : Screen {
    override val key: ScreenKey = uniqueScreenKey

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        // Uncomment version below if you want keep using ViewModel instead of to convert it to ScreenModel
        val viewModel = hiltViewModel<HiltDetailsViewModel, HiltDetailsViewModel.Factory>(
            viewModelFactory = { factory -> factory.create(index) }
        )

        // This version include more boilerplate because we are simulating support
        // to Assisted Injection using ScreenModel. See [HiltListScreen] for a simple version
        /*val viewModel = getScreenModel<HiltDetailsScreenModel, HiltDetailsScreenModel.Factory> { factory ->
            factory.create(index)
        }*/

        DetailsContent(viewModel, "Item #${viewModel.index}", navigator::pop)
    }
}
