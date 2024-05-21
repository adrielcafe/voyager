# Hilt integration

!!! success
    To use the `getScreenModel` you should first import `cafe.adriel.voyager:voyager-hilt` (see [Setup](../setup.md)).

### @Inject

Add `@Inject` annotation to your `ScreenModel`.

```kotlin
class HomeScreenModel @Inject constructor() : ScreenModel {
    // ...
}
```

Call `getScreenModel()` to get a new instance.

```kotlin
class HomeScreen : AndroidScreen() {

    @Composable
    override fun Content() {
        val screenModel = getScreenModel<HomeScreenModel>()
        // ...
    }
}
```

### @AssistedInject

Add `@AssistedInject` annotation to your `ScreenModel` and provide a `ScreenModelFactory` annotated with `@AssistedFactory`.

```kotlin
class PostDetailsScreenModel @AssistedInject constructor(
    @Assisted val postId: Long
) : ScreenModel {

    @AssistedFactory
    interface Factory : ScreenModelFactory {
        fun create(postId: Long): PostDetailsScreenModel
    }
}
```

Call `getScreenModel()` and use your factory to create a new instance.

```kotlin
data class PostDetailsScreen(val postId: Long): AndroidScreen() {

    @Composable
    override fun Content() {
        val screenModel = getScreenModel<PostDetailsScreenModel, PostDetailsScreenModel.Factory> { factory ->
            factory.create(postId)
        }
        // ...
    }
}
```

### Sample

!!! info
    Sample code [here](https://github.com/adrielcafe/voyager/tree/main/samples/android/src/main/java/cafe/adriel/voyager/sample/hiltIntegration).

### Navigator scoped ScreenModel

```kotlin
class HomeScreen : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val screenModel = navigator.getNavigatorScreenModel<HomeScreenModel>()
        // ...
    }
}
```
