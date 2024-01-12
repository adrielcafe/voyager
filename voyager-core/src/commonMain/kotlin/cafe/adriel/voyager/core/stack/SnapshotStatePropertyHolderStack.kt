package cafe.adriel.voyager.core.stack

import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList

public open class SnapshotStatePropertyHolderStack<Item>(
    items: List<Item>,
    minSize: Int = 0
) : PropertyHolderStack<Item> {

    init {
        require(minSize >= 0) { "Min size $minSize is less than zero" }
        require(items.size >= minSize) { "Stack size ${items.size} is less than the min size $minSize" }
    }

    @PublishedApi
    internal val stateStack: SnapshotStateList<Item> = items.toMutableStateList()

    public override val items: List<Item> by derivedStateOf {
        stateStack.toList()
    }

    public override val lastItemOrNull: Item? by derivedStateOf {
        stateStack.lastOrNull()
    }

    public override val size: Int by derivedStateOf {
        stateStack.size
    }

    public override val isEmpty: Boolean by derivedStateOf {
        stateStack.isEmpty()
    }

    public override val canPop: Boolean by derivedStateOf {
        stateStack.size > minSize
    }
}
