package cafe.adriel.voyager.sample.androidViewModel

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import cafe.adriel.voyager.core.screen.uniqueScreenKey
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.sample.DetailsContent
import org.koin.androidx.compose.getViewModel
import org.koin.core.parameter.parametersOf

data class AndroidDetailsScreen(
    val index: Int
) : Screen {
    override val key: ScreenKey = uniqueScreenKey

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = getViewModel<AndroidDetailsViewModel> { parametersOf(index) }

        DetailsContent(viewModel, "Item #${viewModel.index}", navigator::pop)
    }
}
