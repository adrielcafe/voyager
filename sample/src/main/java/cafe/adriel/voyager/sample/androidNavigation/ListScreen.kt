package cafe.adriel.voyager.sample.androidNavigation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ListItem
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.androidx.AndroidScreen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import org.koin.androidx.compose.getStateViewModel

class ListScreen : AndroidScreen() {

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = getStateViewModel<ListViewModel>()

        LazyColumn {
            itemsIndexed(viewModel.items) { index, item ->
                ListItem(
                    text = { Text(text = item) },
                    modifier = Modifier.clickable { navigator.push(DetailsScreen(index)) }
                )
            }
        }
    }
}
