package cafe.adriel.voyager.sample

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ListItem
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.util.UUID

val sampleItems: List<String>
    get() = (0..99).map { "Item #$it | ${UUID.randomUUID().toString().substringBefore('-')}" }

@Composable
fun LoadingContent() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        CircularProgressIndicator()
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ListContent(items: List<String>, onClick: ((Int) -> Unit)? = null) {
    LazyColumn {
        itemsIndexed(items) { index, item ->
            ListItem(
                text = { Text(text = item) },
                modifier = if (onClick == null) Modifier else Modifier.clickable { onClick(index) }
            )
        }
    }
}

@Composable
fun DetailsContent(instance: Any, item: String, onClick: () -> Unit) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = item,
            style = MaterialTheme.typography.h5
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = instance.toString().substringAfterLast('.'),
            style = MaterialTheme.typography.body2
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onClick,
            content = { Text(text = "Back") }
        )
    }
}
