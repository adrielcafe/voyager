package cafe.adriel.voyager.sample.hiltIntegration

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import cafe.adriel.voyager.core.screen.uniqueScreenKey
import cafe.adriel.voyager.hilt.getScreenModel
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
        // ViewModelProvider.Factory is not required. Until now Hilt has no support to Assisted Injection by default
        // val viewModel: HiltDetailsViewModel = getViewModel<HiltDetailsViewModel, HiltDetailsViewModel.Factory> { factory -> factory.create(index) }

        // This version include more boilerplate because we are simulating support
        // to Assisted Injection using ScreenModel. See [HiltListScreen] for a simple version
        val viewModel = getScreenModel<HiltDetailsScreenModel, HiltDetailsScreenModel.Factory> { factory ->
            factory.create(index)
        }

        DetailsContent(viewModel, "Item #${viewModel.index}", navigator::pop)
    }
}
