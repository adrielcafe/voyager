# Back press

By default, Voyager will handle back presses but you can override its behavior. Use the `onBackPressed` to manually handle it: return `true` to pop the current screen, or false otherwise. To disable, just set to `null`.

```kotlin
setContent {
    Navigator(
        initialScreen = HomeScreen,
        onBackPressed = { currentScreen ->
            false // won't pop the current screen
            // true will pop, default behavior
        }
        // To disable:
        // onBackPressed = null
    )
}
```
