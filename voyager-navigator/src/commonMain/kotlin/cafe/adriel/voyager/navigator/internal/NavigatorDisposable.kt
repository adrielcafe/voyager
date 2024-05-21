package cafe.adriel.voyager.navigator.internal

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import cafe.adriel.voyager.core.lifecycle.DisposableEffectIgnoringConfiguration
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import cafe.adriel.voyager.core.stack.StackEvent
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.lifecycle.NavigatorLifecycleStore
import kotlinx.coroutines.channels.Channel

private val disposableEvents: Set<StackEvent> =
    setOf(StackEvent.Pop, StackEvent.Replace)

private data class ScreenData(
    val key: ScreenKey,
    val screen: Screen
)

@Composable
internal fun NavigatorDisposableEffect(
    navigator: Navigator
) {
    DisposableEffectIgnoringConfiguration(navigator) {
        onDispose {
            disposeNavigator(navigator)
        }
    }
}

@Composable
internal fun StepDisposableEffect(
    navigator: Navigator
) {
    val currentScreens = navigator.items

    DisposableEffect(currentScreens) {
        onDispose {
            val newScreenKeys = navigator.items.map { it.key }
            if (navigator.lastEvent in disposableEvents) {
                currentScreens.filter { it.key !in newScreenKeys }.forEach {
                    navigator.dispose(it)
                }
                navigator.clearEvent()
            }
        }
    }
}

@Composable
internal fun StepDisposableAfterTransitionEffect(
    transitionFinishedEvents: Channel<Unit>,
    navigator: Navigator
) {
    val screenCandidatesToDispose = rememberSaveable(saver = screenCandidatesToDisposeSaver()) {
        mutableStateOf(emptySet())
    }

    val currentScreens = navigator.items

    DisposableEffect(currentScreens) {
        onDispose {
            val newScreenKeys = navigator.items.map { it.key }
            screenCandidatesToDispose.value += currentScreens.filter { it.key !in newScreenKeys }
                .map { ScreenData(it.key, it) }
        }
    }

    LaunchedEffect(Unit) {
        for (event in transitionFinishedEvents) {
            val newScreens = navigator.items.map { it.key }
            val screensToDispose = screenCandidatesToDispose.value.filterNot { it.key in newScreens }
            if (screensToDispose.isNotEmpty()) {
                screensToDispose.forEach { navigator.dispose(it.screen) }
                navigator.clearEvent()
            }
            screenCandidatesToDispose.value = emptySet()
        }
    }
}

@Composable
internal fun ChildrenNavigationDisposableEffect(
    navigator: Navigator
) {
    // disposing children navigators
    DisposableEffectIgnoringConfiguration(navigator) {
        onDispose {
            fun disposeChildren(navigator: Navigator) {
                disposeNavigator(navigator)
                navigator.children.values.forEach { childNavigator ->
                    disposeChildren(childNavigator)
                }
                navigator.children.clear()
            }
            if (navigator.parent == null || navigator.disposeBehavior.disposeNestedNavigators) {
                navigator.children.values.forEach { childNavigator ->
                    disposeChildren(childNavigator)
                }
            }
        }
    }

    // referencing nested navigators in parent navigator
    DisposableEffectIgnoringConfiguration(navigator) {
        navigator.parent?.children?.put(navigator.key, navigator)
        onDispose {
            if (navigator.parent?.disposeBehavior?.disposeNestedNavigators != false) {
                navigator.parent?.children?.remove(navigator.key)
            }
        }
    }
}

internal fun disposeNavigator(navigator: Navigator) {
    for (screen in navigator.items) {
        navigator.dispose(screen)
    }
    NavigatorLifecycleStore.remove(navigator)
    navigator.clearEvent()
}

private fun screenCandidatesToDisposeSaver(): Saver<MutableState<Set<ScreenData>>, List<ScreenData>> {
    return Saver(
        save = { it.value.toList() },
        restore = { mutableStateOf(it.toSet()) }
    )
}
