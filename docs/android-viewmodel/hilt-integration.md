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
        val viewModel = getViewModel<HomeViewModel>()
        // ...
    }
}
```

### @AssistedInject

Add `@HiltViewModel` with the `assistedFactory` parameter, and `@AssistedInject` annotations to your `ViewModel`.

```kotlin
@HiltViewModel(assistedFactory = HomeViewModel.Factory::class)
class HomeViewModel @AssistedInject constructor(
    @Assisted val itemId: Long
): ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(itemId: Long): HomeViewModel
    }
}
```

Call `getViewModel()` with the `viewModelFactory` parameter to pass the assisted parameter and get a new instance.

```kotlin
class HomeScreen(private val itemId: Long) : Screen {

    @Composable
    override fun Content() {
        val viewModel = getViewModel<HomeViewModel> { vm: HomeViewModel.Factory ->
            vm.create(itemId)
        }
        // ...
    }
}
```

### Sample

!!! info
    Sample code [here](https://github.com/adrielcafe/voyager/tree/main/samples/android/src/main/java/cafe/adriel/voyager/sample/hiltIntegration).
