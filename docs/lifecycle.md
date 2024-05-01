# Lifecycle

** Experimental API

Inside a `Screen`, you can call `LifecycleEffectOnce` to execute a block of code the first time the Screen appears:

```kotlin
class PostListScreen : Screen {

    @Composable
    override fun Content() {
        LifecycleEffectOnce {
            screenModel.initSomething()
        }

        // ...
    }
}
```



### ScreenLifecycleOwner

&#x20;With `ScreenLifecycleProvider` interface you can provide a special `ScreenLifecycleOwner` that can react when the Screen leaves the Stack

```kotlin
class MyScreenLifecycleOwner : ScreenLifecycleOwner {
    override fun onDispose(screen: Screen) {
        println("My ${screen.key} is being disposed")
    }
}


data object MyScreen : Screen, ScreenLifecycleProvider {
   @Composable
   fun Content() {
      ...
   }
   
   public override fun getLifecycleOwner(): ScreenLifecycleOwner =
       ScreenLifecycleOwner()
}
```

### Extending the Lifecycle lazily

You can access directly the `ScreenLifecycleStore` to add a `ScreenDisposable` implementation lazily in a lifecycle aware custom API.

If you for example have a custom dependency that should be notified when the Screen is disposed you can use the `ScreenLifecycleStore` directly. For example, the Screen Model and ScreenModelStore APIs rely on this API to Store the ScreenModel instance and the Screen owns the ScreenModel instance,  when the Screen leaves the Stack, the ScreenModelStore, that implements `ScreenDisposable` is notified and can dispose the ScreenModel.

Let's imagine a dependency called `MyDependency` that holds and provides a State for a Screen while it is on the Navigator stack and we want to notify `MyDependency` when the Screen leaves the Stack.

```kotlin
class MyCustomAPIWithDisposable(
   private val myDependency: MyDependency
) : ScreenDisposable {
   public override fun onDispose(screen: Screen) {
      myDependency.disposeStateForScreen(screen)
   }
}

@Composable
fun rememberMyDependency(): MyDependency {
   val navigator = LocalNavigator.currentOrThrow
   val myDependency by getMyDependecy() // getting from DI
   remember(myDependency) {
      myDependency.provideStateForScreen(navigator.lastItem)
      ScreenLifecycleStore.get(navigator.lastItem) {
         MyCustomAPIWithDisposable(myDependency)
      }
   }
   
   return myDependency
}
```

### ScreenDisposable for all Screens

You can also provide a `ScreenLifecycleOwner` for all Screen in the stack of a Navigator easily by in the Navigator composable start to listen to `lastItem` for registering at `ScreenLifecycleStore.`

Let's reuse the `MyScreenLifecycleOwner` from the example above and provide it to all screens in the navigator.

```kotlin
class MyScreenLifecycleOwner : ScreenDisposable {
    override fun onDispose(screen: Screen) {
        println("My ${screen.key} is being disposed")
    }
}

@Composable
fun YourAppComposable() {
  Navigator(...) { navigator ->
     remember(navigator.lastItem) {
        ScreenLifecycleStore.get(navigator.lastItem) {
           MyScreenLifecycleOwner()
        }
     }
     CurrentScreen()
  }
}
```
