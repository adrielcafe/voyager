package cafe.adriel.voyager.sample.androidNavigation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ListItem
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.androidx.AndroidScreen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow

class ListScreen : AndroidScreen() {

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        LazyColumn {
            items(100) { index ->
                ListItem(
                    text = { Text(text = "Item $index") },
                    modifier = Modifier.clickable { navigator.push(DetailsScreen(index)) }
                )
            }
        }
    }
}
