package cafe.adriel.voyager.navigator.internal

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.saveable.LocalSaveableStateRegistry
import androidx.compose.runtime.saveable.rememberSaveable
import cafe.adriel.voyager.core.lifecycle.DisposableEffectIgnoringConfiguration
import cafe.adriel.voyager.core.lifecycle.SavedState
import cafe.adriel.voyager.core.lifecycle.ScreenLifecycleOwner
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.stack.StackEvent
import cafe.adriel.voyager.navigator.Navigator

private val disposableEvents: Set<StackEvent> =
    setOf(StackEvent.Pop, StackEvent.Replace)

@Composable
internal fun NavigatorDisposableEffect(
    navigator: Navigator
) {
    DisposableEffectIgnoringConfiguration(navigator) {
        onDispose {
            for (screen in navigator.items) {
                navigator.dispose(screen)
            }
            navigator.clearEvent()
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
internal fun LifecycleDisposableEffect(
    lifecycleOwner: ScreenLifecycleOwner
) {
    val savedState = rememberSaveable { SavedState() }
    if (!lifecycleOwner.isCreated) {
        lifecycleOwner.onCreate(savedState) // do this in the UI thread to force it to be called before anything else
    }

    DisposableEffect(lifecycleOwner) {
        lifecycleOwner.registerLifecycleListener(savedState)
        lifecycleOwner.onStart()
        onDispose {
            lifecycleOwner.performSave(savedState)
            lifecycleOwner.onStop()
        }
    }
}
