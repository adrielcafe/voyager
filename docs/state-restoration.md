# State restoration

Voyager by default expect that it screens can be stored inside a [Bundle](https://developer.android.com/guide/components/activities/parcelables-and-bundles). This means both Java Serializable and Parcelable are supported. By default all Voyager Screen is Java Serializable this means that Screen can be restored if all parameters are Java Serializable.

### Java Serializable

```kotlin
// ✔️ DO
data class Post(/*...*/) : Serializable

data class ValidScreen(
    val userId: UUID, // Built-in serializable types
    val post: Post // Your own serializable types
) : Screen {
    // ...
}

// 🚫 DON'T
class Post(/*...*/)

data class InvalidScreen(
    val context: Context, // Built-in non-serializable types
    val post: Post, // Your own non-serializable types
    val parcelable: SomeParcelable // Android Parcelable is not Java Serializable by default
) : Screen {
    // ...
}
```

Not only the params, but the properties will also be restored, so the same rule applies.

```kotlin
// ✔️ DO
class ValidScreen : Screen {
    
    // Serializable properties
    val tag = "ValidScreen"
    
    // Lazily initialized serializable types
    val randomId by lazy { UUID.randomUUID() }
}

// 🚫 DON'T
class InvalidScreen : Screen {

    // Non-serializable properties
    val postService = PostService()
}
```

### Android Parcelables

```kotlin
// ✔️ DO
@Parcelize
data class Post(/*...*/) : Parcelable

@Parcelize
data class ValidScreen(
    val post: Post // Your own parcelable types
) : Screen, Parcelable {
    // ...
}

// 🚫 DON'T
class Post(/*...*/)

@Parcelize
data class InvalidScreen(
    val context: Context, // Built-in non-parcelable types
    val post: Post, // Your own non-parcelable types
    val serializable: SomeSerializable // Java Serializable are not Android Parcelable by default
) : Screen, Parcelable {
    // ...
}
```

#### Enforcing Android Parcelable on your screens

You can build your own Screen type for enforcing in at compile time that all yours screens should be Parcelable by creating an interface that implement Parcelable.

```kotlin
interface ParcelableScreen : Screen, Parcelable

// Compile
@Parcelize
data class Post(/*...*/) : Parcelable

@Parcelize
data class ValidScreen(
    val post: Post
) : ParcelableScreen {
    // ...
}

// Not compile
data class Post(/*...*/)

@Parcelize
data class ValidScreen(
    val post: Post
) : ParcelableScreen {
    // ...
}
```

Starting from version 1.0.0-rc05 you can specify a custom NavigatorSaver to enforce that all Screen is Parcelable by using [`parcelableNavigatorSaver`](https://github.com/adrielcafe/voyager/blob/main/voyager-navigator/src/androidMain/kotlin/cafe/adriel/voyager/navigator/internal/NavigatorSaver.android.kt#L12).

```kotlin
CompositionLocalProvider(
    LocalNavigatorSaver provides parcelableNavigatorSaver()
) {
    Navigator(...) {
       ...
    }
}
```

### Multiplatform state restoration

When working in a Multiplatform project and sharing the Parameters models with other platforms, your types required to be serializable in a  [Bundle](https://developer.android.com/guide/components/activities/parcelables-and-bundles) if you are targeting Android, the easiest way is defining in common code a `JavaSerializable` interface that on Android only would implement `java.io.Serialiable`, see example below.

```kotlin
// commonMain - module core
expect interface JavaSerializable

data class Post(/*...*/) : JavaSerializable

// androidMain - module core
actual typealias JavaSerializable = java.io.Serializable

// non AndroidMain (ios, web, etc) - module core
actual interface JavaSerializable

// android ui module or compose multiplatform module
data class ValidScreen(
    val post: Post
) : Screen
```

### Dependency Injection

If you want to inject dependencies through a DI framework, make sure it supports Compose, like [Koin](https://insert-koin.io/docs/reference/koin-android/compose/) and [Kodein](https://docs.kodein.org/kodein-di/7.6/framework/compose.html).

```kotlin
// ✔️ DO
class ValidScreen : Screen {
    
    @Composable
    override fun Content() {
        // Inject your dependencies inside composables
        val postService = get<PostService>()
    }
}

// 🚫 DON'T
class InvalidScreen : Screen {

    // Using DI to inject non-serializable types as properties
    val postService by inject<PostService>()
}
```

### Identifying screens

The `Screen` interface has a `key` property used for [saving and restoring the states](https://developer.android.com/reference/kotlin/androidx/compose/runtime/saveable/SaveableStateHolder#SaveableStateProvider\(kotlin.Any,kotlin.Function0\)) for the subtree. You can override the default value to set your own key.

```kotlin
class HomeScreen : Screen {

    override val key = "CUSTOM_KEY"

    @Composable
    override fun Content() {
        // ...
    }
}
```

Voyager provides a `uniqueScreenKey` property, useful if you don't want to manage the keys yourself.

```kotlin
override val key = uniqueScreenKey
```

!!! warning
    You should **always** set your own key if the screen:

    * Is used multiple times in the same `Navigator`
    * Is an [anonymous](https://kotlinlang.org/docs/object-declarations.html#object-expressions) or [local](https://kotlinlang.org/spec/declarations.html#local-class-declaration) class
