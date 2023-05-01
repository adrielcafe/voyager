package cafe.adriel.voyager.core.lifecycle

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import cafe.adriel.voyager.core.annotation.ExperimentalVoyagerApi
import cafe.adriel.voyager.core.annotation.InternalVoyagerApi
import cafe.adriel.voyager.core.screen.Screen

public interface ScreenLifecycleOwner {
    @Composable
    public fun ProvideBeforeScreenContent(
        provideSaveableState: @Composable (suffixKey: String, content: @Composable () -> Unit) -> Unit,
        content: @Composable () -> Unit
    ): Unit = content()

    public fun onDispose(screen: Screen) {}
}

@InternalVoyagerApi
public object DefaultScreenLifecycleOwner : ScreenLifecycleOwner

@ExperimentalVoyagerApi
@InternalVoyagerApi
public class ComposedScreenLifecycleOwner(
    private val screenLifecycleOwners: List<ScreenLifecycleOwner>
) : ScreenLifecycleOwner {

    @Composable
    public fun RecursiveProvideBeforeScreenContent(
        screenLifecycleOwner: ScreenLifecycleOwner,
        provideSaveableState: @Composable (suffixKey: String, content: @Composable () -> Unit) -> Unit,
        content: @Composable () -> Unit,
        nextOrNull: () -> ScreenLifecycleOwner?,
    ) {
        val next = remember(screenLifecycleOwner, provideSaveableState, content, nextOrNull) { nextOrNull() }
        if(next != null) {
            val recursiveContent = @Composable {
                RecursiveProvideBeforeScreenContent(
                    screenLifecycleOwner = next,
                    provideSaveableState = provideSaveableState,
                    content = content,
                    nextOrNull = nextOrNull,
                )
            }
            screenLifecycleOwner.ProvideBeforeScreenContent(
                provideSaveableState = { suffixKey, _ ->
                    provideSaveableState(suffixKey, recursiveContent)
                }
            ) {
                recursiveContent()
            }

        } else {
            screenLifecycleOwner.ProvideBeforeScreenContent(
                provideSaveableState = { suffixKey, content ->
                    provideSaveableState(suffixKey, content)
                }
            ) {
                content()
            }
        }
    }

    @Composable
    override fun ProvideBeforeScreenContent(
        provideSaveableState: @Composable (suffixKey: String, content: @Composable () -> Unit) -> Unit,
        content: @Composable () -> Unit
    ) {
        if(screenLifecycleOwners.isNotEmpty()) {
            val copy = screenLifecycleOwners.toMutableList()
            RecursiveProvideBeforeScreenContent(
                screenLifecycleOwner = copy.removeFirst(),
                provideSaveableState = provideSaveableState,
                content = content,
                nextOrNull = { copy.removeFirstOrNull() }
            )
        } else {
            content()
        }
    }

    override fun onDispose(screen: Screen) {
        for (screenLifecycleOwner in screenLifecycleOwners) {
            screenLifecycleOwner.onDispose(screen)
        }
    }
}
