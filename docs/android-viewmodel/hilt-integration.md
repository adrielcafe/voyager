# Hilt integration

!!! success
    To use the `getViewModel` you should first import `cafe.adriel.voyager:voyager-hilt` (see [Setup](../setup.md)).

### @Inject

Add `@HiltViewModel` and `@Inject` annotations to your `ViewModel`.

```kotlin
@HiltViewModel
class HomeViewModel @Inject constructor() : ViewModel() {
    // ...
}
```

Call `getViewModel()` to get a new instance.

```kotlin
class HomeScreen : Screen {

    @Composable
    override fun Content() {
        val screenModel = getViewModel<HomeScreenModel>()
        // ...
    }
}
```

### @AssistedInject

Currently there's no Assisted Injection support for Hilt ViewModels ([issue](https://github.com/google/dagger/issues/2287)).

### Sample

!!! info
    Sample code [here](https://github.com/adrielcafe/voyager/tree/main/samples/android/src/main/java/cafe/adriel/voyager/sample/hiltIntegration).
