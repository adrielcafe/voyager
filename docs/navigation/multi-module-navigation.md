# Multi-module navigation

Voyager has built-in support for multi-module navigation. Its API is based on great DI frameworks like [Koin](https://github.com/InsertKoinIO/koin) and [Kodein](https://github.com/Kodein-Framework/Kodein-DI/), so should be familiar to use.

Suppose we have the following modules:

* `app`: the entrypoint of our app, contains the Activity
* `feature-home`: contains the root screen (`HomeScreen`)
* `feature-posts`: contains screens related to the posts feature (`ListScreen` and `DetailsScreen`)
* `navigation`: contains the screen providers used to navigate between modules, both `feature-home` and `feature-posts` imports it

To navigate from `HomeScreen` to the screens on `feature-posts` module (`ListScreen` and `DetailsScreen`), we first need to provide them. The `navigation` module should declare all shared screens in the app,  use the `ScreenProvider` interface for that.

```kotlin title="navigation/../SharedScreen.kt"
sealed class SharedScreen : ScreenProvider {
    object PostList : SharedScreen()
    data class PostDetails(val id: String) : SharedScreen()
}
```

Now use the `ScreenRegistry` to register these providers. This should be done in your`Application` class.

```kotlin title="app/../MyApp.kt"
class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()

        ScreenRegistry {
            register<SharedScreen.PostList> {
                ListScreen()
            }
            register<SharedScreen.PostDetails> { provider ->
                DetailsScreen(id = provider.id)
            }
        }
    }
}
```

You can also create a `screenModule` into your feature module, that way your `Application` class won't be bloated by too many declarations.

=== "ScreenModule"

    ```kotlin title="feature-posts/../ScreenModule.kt"
    val featurePostsScreenModule = screenModule {
        register<SharedScreen.PostList> {
            ListScreen()
        }
        register<SharedScreen.PostDetails> { provider ->
            DetailsScreen(id = provider.id)
        }
    }
    ```

    ```kotlin title="app/../MyApp.kt"
    override fun onCreate() {
        super.onCreate()
    
        ScreenRegistry {
            featurePostsScreenModule()
        }
    }
    ```

Finally, call `rememberScreen()` (inside a composable function) or `ScreenRegistry.get()` to access your screens.

```kotlin title="feature-home/../HomeScreen.kt"
class HomeScreen : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val postListScreen = rememberScreen(SharedScreen.PostList)
        val postDetailsScreen = rememberScreen(SharedScreen.PostDetails(id = postId))
        
        // navigator.push(postListScreen)
        // navigator.push(postDetailsScreen)
    }
}
```

### **Sample**

![](../media/assets/ezgif.com-gif-maker.gif)

!!! info
    Source code [here](https://github.com/adrielcafe/voyager/tree/main/samples/multi-module).

