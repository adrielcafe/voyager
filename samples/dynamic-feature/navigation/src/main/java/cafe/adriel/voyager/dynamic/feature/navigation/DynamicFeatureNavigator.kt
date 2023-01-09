package cafe.adriel.voyager.dynamic.feature.navigation

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.CurrentScreen
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.NavigatorContent
import cafe.adriel.voyager.navigator.NavigatorDisposeBehavior
import cafe.adriel.voyager.navigator.OnBackPressed

@Composable
fun DynamicFeatureNavigator(
    activity: Activity, // Activity is required when there is a require for user confirmation
    screen: Screen,
    disposeBehavior: NavigatorDisposeBehavior = NavigatorDisposeBehavior(),
    onBackPressed: OnBackPressed = { true },
    content: NavigatorContent = { CurrentScreen() }
) {
    DynamicFeatureNavigator(
        activity = activity,
        screens = listOf(screen),
        disposeBehavior = disposeBehavior,
        onBackPressed = onBackPressed,
        content = content
    )
}

@Composable
fun DynamicFeatureNavigator(
    activity: Activity, // Activity is required when there is a require for user confirmation
    screens: List<Screen>,
    disposeBehavior: NavigatorDisposeBehavior = NavigatorDisposeBehavior(),
    onBackPressed: OnBackPressed = { true },
    content: NavigatorContent = { CurrentScreen() }
) {
    val manager = remember {
        DynamicFeatureManager(activity)
    }

    LaunchedEffect(key1 = manager) {
        manager.register()
    }

    DisposableEffect(key1 = manager) {
        onDispose {
            manager.unregister()
        }
    }

    Navigator(
        screens = screens,
        disposeBehavior = disposeBehavior,
        onBackPressed = onBackPressed,
    ) { navigator ->
        val lastScreen = navigator.lastItem
        if (lastScreen is DynamicFeatureScreen) {
            lastScreen.currentProvider = manager
        }
        content(navigator)
    }
}
