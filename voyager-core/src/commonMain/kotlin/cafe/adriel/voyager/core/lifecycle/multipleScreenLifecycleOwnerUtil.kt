package cafe.adriel.voyager.core.lifecycle

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import cafe.adriel.voyager.core.annotation.InternalVoyagerApi
import cafe.adriel.voyager.core.internal.removeFirstElement
import cafe.adriel.voyager.core.internal.removeFirstElementOrNull

@InternalVoyagerApi
@Composable
public fun MultipleProvideBeforeScreenContent(
    screenLifecycleContentProviders: List<ScreenLifecycleContentProvider>,
    provideSaveableState: @Composable (suffixKey: String, content: @Composable () -> Unit) -> Unit,
    content: @Composable () -> Unit
) {
    if (screenLifecycleContentProviders.isNotEmpty()) {
        val copy = screenLifecycleContentProviders.toMutableList()
        RecursiveProvideBeforeScreenContent(
            screenLifecycleContentProvider = copy.removeFirstElement(),
            provideSaveableState = provideSaveableState,
            content = content,
            nextOrNull = { copy.removeFirstElementOrNull() }
        )
    } else {
        content()
    }
}

@Composable
private fun RecursiveProvideBeforeScreenContent(
    screenLifecycleContentProvider: ScreenLifecycleContentProvider,
    provideSaveableState: @Composable (suffixKey: String, content: @Composable () -> Unit) -> Unit,
    content: @Composable () -> Unit,
    nextOrNull: () -> ScreenLifecycleContentProvider?
) {
    val next = remember(screenLifecycleContentProvider, provideSaveableState, content, nextOrNull) { nextOrNull() }
    if (next != null) {
        val recursiveContent = @Composable {
            RecursiveProvideBeforeScreenContent(
                screenLifecycleContentProvider = next,
                provideSaveableState = provideSaveableState,
                content = content,
                nextOrNull = nextOrNull
            )
        }
        screenLifecycleContentProvider.ProvideBeforeScreenContent(
            provideSaveableState = { suffixKey, _ ->
                provideSaveableState(suffixKey, recursiveContent)
            }
        ) {
            recursiveContent()
        }
    } else {
        screenLifecycleContentProvider.ProvideBeforeScreenContent(
            provideSaveableState = { suffixKey, content ->
                provideSaveableState(suffixKey, content)
            }
        ) {
            content()
        }
    }
}
