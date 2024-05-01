# ViewModel KMP

Since 1.1.0-beta01 we have introduce a experimental API for ViewModel KMP. It is under the package `cafe.adriel.voyager:voyager-lifecycle-kmp`  (see [Setup](../setup.md)).

You will need to call `ProvideNavigatorLifecycleKMPSupport` before all `Navigator` calls and it will be working out of the box.

```kotlin
@Composable
fun MainView() {
    ProvideNavigatorLifecycleKMPSupport {
        Navigator(...)
    }
}

class MyScreen : Screen {
    @Composable
    fun Content() {
        val myViewModel = viewModel { MyScreenViewModel() }
    }
}
```

## Navigator scoped ViewModel

Voyager 1.1.0-beta01 also have introduced the support for Navigator scoped ViewModel and Lifecycle.
This will make easy to share a ViewModel cross screen of the same navigator.

```kotlin
class MyScreen : Screen {
    @Composable
    fun Content() {
        val myViewModel = navigatorViewModel { MyScreenViewModel() }
    }
}
```

## Lifecycle KMP

This version also brings the Lifecycle events for Screen lifecycle in KMP, now is possible to
a generic third party API that listen to Lifecycle of a Screen in KMP.
