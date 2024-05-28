# Kodein integration

!!! success
    To use the `rememberScreenModel` you should first import `cafe.adriel.voyager:voyager-kodein` (see [Setup](../setup.md)).

Declare your `ScreenModel`s using the `bindProvider` bind.

```kotlin
val homeModule = DI.Module(name = "home") {
    bindProvider { HomeScreenModel() } 
}
```

Call `rememberScreenModel()` to get a new instance.

```kotlin
class HomeScreen : Screen {

    @Composable
    override fun Content() {
        val screenModel = rememberScreenModel<HomeScreenModel>()
        // ...
    }
}
```

### Sample

!!! info
    Sample code [here](https://github.com/adrielcafe/voyager/tree/main/samples/android/src/main/java/cafe/adriel/voyager/sample/kodeinIntegration).

### Navigator scoped ScreenModel

```kotlin
class HomeScreen : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val screenModel = navigator.rememberNavigatorScreenModel<HomeScreenModel>()
        // ...
    }
}
```
