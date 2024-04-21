package cafe.adriel.voyager.navigator.bottomSheet

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.contentColorFor
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.annotation.InternalVoyagerApi
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.stack.Stack
import cafe.adriel.voyager.navigator.CurrentScreen
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.bottomSheet.internal.BottomSheetNavigatorBackHandler
import cafe.adriel.voyager.navigator.compositionUniqueId
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

public typealias BottomSheetNavigatorContent = @Composable (bottomSheetNavigator: BottomSheetNavigator) -> Unit

public val LocalBottomSheetNavigator: ProvidableCompositionLocal<BottomSheetNavigator> =
    staticCompositionLocalOf { error("BottomSheetNavigator not initialized") }

@ExperimentalMaterial3Api
@Composable
public fun BottomSheetNavigator(
    onDismissRequest: () -> Unit = {},
    modifier: Modifier = Modifier,
    hideOnBackPress: Boolean = true,
    scrimColor: Color = BottomSheetDefaults.ScrimColor,
    sheetShape: Shape = MaterialTheme.shapes.large,
    tonalElevation: Dp = BottomSheetDefaults.Elevation,
    containerColor: Color = MaterialTheme.colorScheme.surface,
    contentColor: Color = contentColorFor(containerColor),
    skipPartiallyExpanded: Boolean = true,
    key: String = compositionUniqueId(),
    sheetContent: BottomSheetNavigatorContent = { CurrentScreen() },
    content: BottomSheetNavigatorContent
) {
    var hideBottomSheet: (() -> Unit)? = null
    val coroutineScope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = skipPartiallyExpanded
    )

    LaunchedEffect(sheetState, sheetState.currentValue) {
        if (sheetState.currentValue == SheetValue.Hidden) {
            hideBottomSheet?.invoke()
        }
    }

    Navigator(HiddenBottomSheetScreen, onBackPressed = null, key = key) { navigator ->
        val bottomSheetNavigator = remember(navigator, sheetState, coroutineScope) {
            BottomSheetNavigator(navigator, sheetState, coroutineScope)
        }

        hideBottomSheet = bottomSheetNavigator::hide

        CompositionLocalProvider(LocalBottomSheetNavigator provides bottomSheetNavigator) {
            content(bottomSheetNavigator)

            if (sheetState.isVisible) {
                ModalBottomSheet(
                    onDismissRequest = onDismissRequest,
                    modifier = modifier,
                    sheetState = sheetState,
                    shape = sheetShape,
                    containerColor = containerColor,
                    contentColor = contentColor,
                    tonalElevation = tonalElevation,
                    scrimColor = scrimColor,
                    content = {
                        BottomSheetNavigatorBackHandler(bottomSheetNavigator, sheetState, hideOnBackPress)
                        sheetContent(bottomSheetNavigator)
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
public class BottomSheetNavigator @InternalVoyagerApi constructor(
    private val navigator: Navigator,
    private val sheetState: SheetState,
    private val coroutineScope: CoroutineScope
) : Stack<Screen> by navigator {

    public val isVisible: Boolean
        get() = sheetState.isVisible

    public fun show(screen: Screen) {
        coroutineScope.launch {
            replaceAll(screen)
            sheetState.show()
        }
    }

    public fun hide() {
        coroutineScope.launch {
            if (isVisible) {
                sheetState.hide()
                replaceAll(HiddenBottomSheetScreen)
            } else if (sheetState.targetValue == SheetValue.Hidden) {
                // Swipe down - sheetState is already hidden here so `isVisible` is false
                replaceAll(HiddenBottomSheetScreen)
            }
        }
    }

    @Composable
    public fun saveableState(
        key: String,
        content: @Composable () -> Unit
    ) {
        navigator.saveableState(key, content = content)
    }
}

private object HiddenBottomSheetScreen : Screen {

    @Composable
    override fun Content() {
        Spacer(modifier = Modifier.height(1.dp))
    }
}
