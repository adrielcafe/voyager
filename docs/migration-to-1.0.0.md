---
description: Voyager 1.0.0 brings API stability and the first deprecation cycle.
---

# Migration to 1.0.0

### ScreenModel

#### New Module

Now the ScreenModel API has its own module and no longer are available in `voyager-core`, if you are using ScreenModel you should declare the dependency `cafe.adriel.voyager:voyager-screenmodel` (see [Setup](setup.md)).

#### Navigation level ScreenModel

Since 1.0.0-rc08 we have introduced the ScreenModel scoped at Navigator Lifecycle, now the API is no longer marked as Experimental.

#### Deprecation cycle

Since `1.0.0-rc08` we have renamed the extension `coroutineScope` to `screenModelScope`, now it was removed from 1.0.0, if you are still using it, just replace with `screenModelScope.`

### AndroidScreen

The module `voyager-androidx` and `AndroidScreen` was removed! Since `1.0.0-rc06` we have introduced a new API called `NavigatorScreenLifecycleProvider` that provides by default the previous behavior of `AndroidScreenLifecycleOwner` on Android target for all Screen.

Important notice: AndroidScreen, different from Screen, it holds the `Screen.key` as a `uniqueScreenKey`, this is a pretty common requirement, to avoid issues and weird behaviors, we recommend continuing to specify a `uniqueScreenKey` if you are not, we also recommend creating a `abstract class UniqueScreen` to replace your  `AndroidScreen` implementation.

```kotlin
abstract class UniqueScreen : Screen {
    override val key: ScreenKey = uniqueScreenKey
}
```

### APIs promote to Stable

* All Navigator scoped ScreenModels API
* `NavigatorLifecycleStore` and `NavigatorDisposable`
* `TabDisposable`

### Deprecation cycle

* `ScreenLifecycleStore.get`: Use `register` or new `get` function instead.
* `Stack.lastOrNull`: Use `lastItemOrNull` instead
* `Navigator.last`: Use `lastItem` instead
* `ScreenModel.coroutineScope`: Use `screenModelScope` instead&#x20;
* `ScreenModelStore.remove`: Use `onDispose` instead.
* `Tab.title`: Use `options` instead.
* `Tab.icon`: Use `options` instead.
