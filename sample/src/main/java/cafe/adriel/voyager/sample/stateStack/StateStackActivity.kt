package cafe.adriel.voyager.sample.stateStack

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.stack.rememberStateStack
import java.util.UUID

class StateStackActivity : ComponentActivity() {

    private val randomValue: String
        get() = UUID.randomUUID().toString().substringBefore('-')

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            Content()
        }
    }

    @Composable
    private fun Content() {
        val stateStack = rememberStateStack<String>()
        val (selectedItem, selectItem) = rememberSaveable { mutableStateOf("") }
        val context = LocalContext.current

        Column {
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                reverseLayout = true,
                modifier = Modifier.weight(.8f)
            ) {
                itemsIndexed(stateStack.items) { index, item ->
                    ListItem(
                        index = index,
                        item = item,
                        isLast = stateStack.lastItemOrNull == item,
                        isSelected = selectedItem == item,
                        onSelected = selectItem
                    )
                }
                if (stateStack.isEmpty) {
                    item {
                        Text(
                            text = "EMPTY",
                            textAlign = TextAlign.Center,
                            color = Color.DarkGray,
                            fontWeight = FontWeight.Normal,
                            fontFamily = FontFamily.Monospace,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
            Row(
                modifier = Modifier.weight(.1f)
            ) {
                ActionButton(text = "Pop", enabled = stateStack.canPop) {
                    if (stateStack.lastItemOrNull == selectedItem) {
                        selectItem("")
                    }
                    stateStack.pop()
                }
                ActionButton(text = "Pop Until", enabled = stateStack.canPop) {
                    if (selectedItem.isBlank()) {
                        Toast.makeText(context, "Select an item first", Toast.LENGTH_SHORT).show()
                        return@ActionButton
                    }
                    selectItem("")
                    stateStack.popUntil { it == selectedItem }
                }
                ActionButton(text = "Push") {
                    stateStack.push(randomValue)
                }
            }
            Row(
                modifier = Modifier.weight(.1f)
            ) {
                ActionButton(text = "Replace") {
                    stateStack.replace(randomValue)
                }
                ActionButton(text = "Replace All") {
                    stateStack.replaceAll(randomValue)
                }
            }
        }
    }

    @Composable
    private fun LazyItemScope.ListItem(
        index: Int,
        item: String,
        isLast: Boolean,
        isSelected: Boolean,
        onSelected: (String) -> Unit
    ) {
        val formattedIndex = index.toString().padStart(3, ' ')
        Text(
            text = "$formattedIndex: $item",
            color = if (isSelected) Color.Red else Color.Black,
            fontWeight = if (isLast) FontWeight.Bold else FontWeight.Normal,
            fontFamily = FontFamily.Monospace,
            modifier = Modifier.clickable { onSelected(item) }
        )
    }

    @Composable
    private fun RowScope.ActionButton(
        text: String,
        enabled: Boolean = true,
        onClick: () -> Unit
    ) {
        Button(
            onClick = onClick,
            enabled = enabled,
            modifier = Modifier
                .weight(.1f)
                .padding(8.dp)
        ) {
            Text(text = text)
        }
    }
}
