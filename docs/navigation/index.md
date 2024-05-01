# Navigation

!!! success
    To use the `Navigator` you should first import `cafe.adriel.voyager:voyager-navigator` (see [Setup](../setup.md)).

### Screen

On Voyager, screens are just classes with a composable function as the entrypoint. To create one, you should implement the `Screen` interface and override the `Content()` composable function.

You can use `data class` (if you need to send params), `class` (if no param is required).

```kotlin
class PostListScreen : Screen {

    @Composable
    override fun Content() {
        // ...
    }
}

data class PostDetailsScreen(val postId: Long) : Screen {

    @Composable
    override fun Content() {
        // ...
    }
}
```

### Navigator

`Navigator` is a composable function deeply integrated with Compose internals. It'll manage the [lifecyle](../lifecycle.md), [back press](../back-press.md), [state restoration](../state-restoration.md) and even [nested navigation](nested-navigation.md) for you.

To start using it, just set the initial `Screen`.

```kotlin
class SingleActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            Navigator(HomeScreen)
        }
    }
}
```

Use the `LocalNavigator` to navigate to other screens. Take a look at the [Stack API](../stack-api.md) for the available operations.

```kotlin
class PostListScreen : Screen {

    @Composable
    override fun Content() {
        // ...
    }

    @Composable
    private fun PostCard(post: Post) {
        val navigator = LocalNavigator.currentOrThrow
        
        Card(
            modifier = Modifier.clickable { 
                navigator.push(PostDetailsScreen(post.id))
                // Also works:
                // navigator push PostDetailsScreen(post.id)
                // navigator += PostDetailsScreen(post.id)
            }
        ) {
            // ...
        }
    }
}
```

If part of your UI is shared between screens, like the `TopAppBar` or `BottomNavigation`, you can easily reuse them with Voyager.

```kotlin
@Composable
override fun Content() {
    Navigator(HomeScreen) { navigator ->
        Scaffold(
            topBar = { /* ... */ },
            content = { CurrentScreen() },
            bottomBar = { /* ... */ }
        )
    }
}
```

{% hint style="warning" %}
You should use `CurrentScreen()` instead of `navigator.lastItem.Content()`, because it will save the Screen's subtree for you (see [SaveableStateHolder](https://developer.android.com/reference/kotlin/androidx/compose/runtime/saveable/SaveableStateHolder)).
{% endhint %}

### Sample

![](../media/assets/basic-nav.gif)

!!! info
    Source code [here](https://github.com/adrielcafe/voyager/tree/main/samples/android/src/main/java/cafe/adriel/voyager/sample/basicNavigation).

