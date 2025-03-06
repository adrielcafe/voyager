# ScreenModel



!!! success
    The ScreenModel API is a part of the module`cafe.adriel.voyager:voyager-screenmodel` (see [Setup](../setup.md)).

`ScreenModel` is just like a [ViewModel](../android-viewmodel/): designed to store and manage UI-related data in a lifecycle conscious way. It also allows data to survive configuration changes such as screen rotations.

Unlike `ViewModel`, `ScreenModel` is just an interface. It's also Android independent and doesn't requires an `Activity` or `Fragment` to work.

```kotlin
class HomeScreenModel : ScreenModel {
   
   // Optional
    override fun onDispose() {
        // ...
    }
}
```

!!! info
    `ScreenModel` is integrated with [Coroutines](coroutines-integration.md), [RxJava](rxjava-integration.md), [LiveData](livedata-integration.md), [Koin](koin-integration.md), [Kodein](kodein-integration.md) and [Hilt](hilt-integration.md)!

By design, it's only possible to create a `ScreenModel` instance inside a `Screen`. Call `rememberScreenModel` and provide a factory lambda.

```kotlin
class HomeScreen : Screen {

    @Composable
    override fun Content() {
        val screenModel = rememberScreenModel { HomeScreenModel() }
        // ...
    }
}
```

If you need to have multiple instances of the same `ScreenModel` for the same `Screen`, add a tag to differentiate them.

```kotlin
val screenModel = rememberScreenModel(tag = "CUSTOM_TAG") { HomeScreenModel() }
```

### Sample

![](<../media/assets/ezgif.com-gif-maker (1).gif>)

!!! info
    Source code [here](https://github.com/adrielcafe/voyager/tree/main/samples/android/src/main/java/cafe/adriel/voyager/sample/screenModel).


### Navigator scoped ScreenModel

!!! success
    The`rememberNavigatorScreenModel` are part of the navigator library.

Starting from [`1.0.0rc08`](https://github.com/adrielcafe/voyager/releases/tag/1.0.0-rc08) by using the new Navigator extension called `rememberNavigatorScreenModel` is possible to have a ScreenModel that is shared cross all Screens from a Navigator and when the Navigator leaves the Composition the ScreenModel is disposed.

```kotlin
class HomeScreen : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val screenModel = navigator.rememberNavigatorScreenModel { HomeScreenModel() }
        // ...
    }
}
```

!!! info
    Each DI library we provide a extension out of the box it is also provided support for Navigator scoped ScreenModel. See [Koin](koin-integration.md), [Kodein](kodein-integration.md) and [Hit](hilt-integration.md)!

