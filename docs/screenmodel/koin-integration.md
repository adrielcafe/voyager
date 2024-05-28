# Koin integration

!!! success
    To use the `getScreenModel` you should first import `cafe.adriel.voyager:voyager-koin` (see [Setup](../setup.md)).

!!! warning
    Since [1.1.0-alpha04](https://github.com/adrielcafe/voyager/releases/tag/1.1.0-alpha04) we have rename the `getScreenModel` to `koinScreenModel`, this is a change to follow Koin Compose naming schema. The previous `getScreenModel` is deprecated and will be removed on 1.1.0

Declare your `ScreenModel`s using the `factory` component.

```kotlin
val homeModule = module {
    factory { HomeScreenModel() } 
}
```

Call `getScreenModel()` to get a new instance.

```kotlin
class HomeScreen : Screen {

    @Composable
    override fun Content() {
        val screenModel = getScreenModel<HomeScreenModel>()
        // ...
    }
}
```

### Sample

!!! info
    Sample code [here](https://github.com/adrielcafe/voyager/tree/main/samples/android/src/main/java/cafe/adriel/voyager/sample/koinIntegration).

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
