[![Maven metadata URL](https://img.shields.io/maven-metadata/v?color=blue&metadataUrl=https://s01.oss.sonatype.org/service/local/repo_groups/public/content/cafe/adriel/voyager/voyager-core/maven-metadata.xml&style=for-the-badge)](https://repo.maven.apache.org/maven2/cafe/adriel/voyager/)
[![Android API](https://img.shields.io/badge/api-21%2B-brightgreen.svg?style=for-the-badge)](https://android-arsenal.com/api?level=21)
[![kotlin](https://img.shields.io/github/languages/top/adrielcafe/voyager.svg?style=for-the-badge&color=blueviolet)](https://kotlinlang.org/)
[![ktlint](https://img.shields.io/badge/code%20style-%E2%9D%A4-FF4081.svg?style=for-the-badge)](https://ktlint.github.io/)
[![License MIT](https://img.shields.io/github/license/adrielcafe/voyager.svg?style=for-the-badge&color=orange)](https://opensource.org/licenses/MIT)

<h1 align="center">
    <img src="https://user-images.githubusercontent.com/2512298/126322645-7bd31c6c-2001-4a5a-a9c5-3cfabbb19726.png"/>
    <br>
    <a href="https://en.wikipedia.org/wiki/USS_Voyager_(Star_Trek)">Voyager</a>: Compose on Warp Speed
</h1>

**Voyager** is a pragmatic navigation library built for, and seamlessly integrated with, [Jetpack Compose](https://developer.android.com/jetpack/compose). 

Turn on the Warp Drive and enjoy the trek 🖖

#### Features
- [x] Create scalable Single-Activity apps powered by a [pragmatic API](#usage)
- [x] State-aware [Stack API](#stack-api)
- [x] [Tab navigation](#tab-navigation) like [Youtube app](https://play.google.com/store/apps/details?id=com.google.android.youtube)
- [x] [Nested navigation](#nested-navigation) if you need to manage multiple stacks
- [x] [State restoration](#state-restoration) after Activity recreation
- [x] [Lifecycle](#lifecycle) callbacks
- [x] [Back press](#back-press) handling
- [x] [Deep linking](#deep-links) support
- [ ] [Compose for Desktop](https://github.com/JetBrains/compose-jb) support (soon™)

## Setup
Add the desired dependencies to your module's build.gradle:
```groovy
dependencies {
    implementation "cafe.adriel.voyager:voyager-navigator:$currentVersion"
    implementation "cafe.adriel.voyager:voyager-tab-navigator:$currentVersion"
}
```

Current version: ![Maven metadata URL](https://img.shields.io/maven-metadata/v?color=blue&metadataUrl=https%3A%2F%2Fs01.oss.sonatype.org%2Fservice%2Flocal%2Frepo_groups%2Fpublic%2Fcontent%2Fcafe%2Fadriel%2Fvoyager%2Fvoyager-core%2Fmaven-metadata.xml)

## Samples
| [Stack API](https://github.com/adrielcafe/voyager/tree/main/sample/src/main/java/cafe/adriel/voyager/sample/stateStack) | [Basic nav.](https://github.com/adrielcafe/voyager/tree/main/sample/src/main/java/cafe/adriel/voyager/sample/basicNavigation) | [Tab nav.](https://github.com/adrielcafe/voyager/tree/main/sample/src/main/java/cafe/adriel/voyager/sample/tabNavigation) | [Nested nav.](https://github.com/adrielcafe/voyager/tree/main/sample/src/main/java/cafe/adriel/voyager/sample/nestedNavigation) |
|-------|------------|----------|-------------|
| ![navigation-stack](https://user-images.githubusercontent.com/2512298/126323192-9b6349fe-7b96-4acf-b62e-c75165d909e1.gif) | ![navigation-basic](https://user-images.githubusercontent.com/2512298/126323165-47760eec-2ba2-48ee-8e3a-841d50098d33.gif) | ![navigation-tab](https://user-images.githubusercontent.com/2512298/126323588-2f970953-0adb-47f8-b2fb-91c5854656bd.gif) | ![navigation-nested](https://user-images.githubusercontent.com/2512298/126323027-a2633aef-9402-4df8-9384-45935d7986cf.gif) |

## Usage
Let's start by creating the screens: you should implement the [Screen](https://github.com/adrielcafe/voyager/blob/main/voyager-core/src/main/java/cafe/adriel/voyager/core/screen/Screen.kt) interface and override the `Content()` composable function. Screens can be `data class` (if you need to send params), `class` (if no param is required) or even `object` (useful for tabs).
```kotlin
object HomeScreen : Screen {

    @Composable
    override fun Content() {
        // ...
    }
}

class PostListScreen : Screen {

    @Composable
    override fun Content() {
        // ...
    }
}

data class PostDetailsScreen(val postId: Long) : Screen {

    @Composable
    override fun Content() {
        // ...
    }
}
```

Now, start the `Navigator` with the root screen.
```kotlin
class SingleActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            Navigator(HomeScreen)
        }
    }
}
```

Use the `LocalNavigator` to navigate to other screens. Take a look at the [Stack API](#stack-api) for the available operations.
```kotlin
class PostListScreen : Screen {

    @Composable
    override fun Content() {
        // ...
    }

    @Composable
    private fun PostCard(post: Post) {
        val navigator = LocalNavigator.currentOrThrow
        
        Card(
            modifier = Modifier.clickable { 
                navigator.push(PostDetailsScreen(post.id))
                // Also works:
                // navigator push PostDetailsScreen(post.id)
                // navigator += PostDetailsScreen(post.id)
            }
        ) {
            // ...
        }
    }
}
```

### Stack API
Voyager is backed by a [SnapshotStateStack](https://github.com/adrielcafe/voyager/blob/main/voyager-core/src/main/java/cafe/adriel/voyager/core/stack/SnapshotStateStack.kt):
* Implementation of [Stack](https://github.com/adrielcafe/voyager/blob/main/voyager-core/src/main/java/cafe/adriel/voyager/core/stack/Stack.kt) that can be observed and snapshot
* Internally uses a [SnapshotStateList](https://developer.android.com/reference/kotlin/androidx/compose/runtime/snapshots/SnapshotStateList)
* State-aware: content change triggers a [recomposition](https://developer.android.com/jetpack/compose/mental-model#recomposition)

You will use it to navigate forward (`push`, `replace`, `replaceAll`) and backwards (`pop`, `popAll`, `popUntil`), but the `SnapshotStateStack` can also be used as a regular collection.
```kotlin
val stack = mutableStateStackOf("🍇", "🍉", "🍌", "🍐", "🥝", "🍋")
// 🍇, 🍉, 🍌, 🍐, 🥝, 🍋

stack.lastOrNull
// 🍋

stack.push("🍍")
// 🍇, 🍉, 🍌, 🍐, 🥝, 🍋, 🍍

stack.pop()
// 🍇, 🍉, 🍌, 🍐, 🥝, 🍋

stack.popUntil { it == "🍐" }
// 🍇, 🍉, 🍌, 🍐

stack.replace("🍓")
// 🍇, 🍉, 🍌, 🍓

stack.replaceAll("🍒")
// 🍒
```

You can also create a `SnapshotStateStack` through `rememberStateStack()`, it will restore the values after [Activity recreation](#state-restoration).

### State restoration

The `Screen` interface is `Serializable`. Every param of your screens will be saved and restored automatically. 
Because of that, it's important to known what can be passed as param: anything that can be stored inside a [Bundle](https://developer.android.com/guide/components/activities/parcelables-and-bundles).
```kotlin
// ✔️ DO
@Parcelize
data class Post(/*...*/) : Parcelable

data class ValidScreen(
    val userId: UUID, // Built-in serializable types
    val post: Post // Your own parcelable and serializable types
) : Screen {
    // ...
}

// 🚫 DON'T
class Post(/*...*/)

data class InvalidScreen(
    val context: Context, // Built-in non-serializable types
    val post: Post // Your own non-parcelable and non-serializable types
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

    // Lambdas
    val callback = { /*...*/ }
}

// 🚫 DON'T
class InvalidScreen : Screen {

    // Non-serializable properties
    val postService = PostService()
}
```

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

### Lifecycle

Inside a `Screen`, you can call `LifecycleEffect` to listen for some events:
* `onStarted`: called when the screen enters the composition
* `onDisposed`: called when the screen is disposed
```kotlin
class PostListScreen : Screen {

    @Composable
    override fun Content() {
        LifecycleEffect(
            onStarted = { /*...*/ },
            onDisposed = { /*...*/ }
        )

        // ...
    }
}
```

### Back press

By default, Voyager will handle back presses but you can override its behavior. Use the `onBackPressed` to manually handle it: return `true` to pop the current screen, or false otherwise. To disable, just set to `null`.
```kotlin
setContent {
    Navigator(
        initialScreen = HomeScreen,
        onBackPressed = { currentScreen ->
            false // won't pop the current screen
            // true will pop, default behavior
        }
        // To disable:
        // onBackPressed = null
    )
}
```

### Deep links

You can initialize the `Navigator` with multiple screens, that way, the first visible screen will be the last one and will be possible to return (`pop()`) to the previous screens.
```kotlin
val postId = getPostIdFromIntent()

setContent {
    Navigator(
        HomeScreen,
        PostListScreen(),
        PostDetailsScreen(postId)
    )
}
```

### Transitions

It's simple to add transition between screens: when initializing the `Navigator` you can override the default content. You can use, for example, the built-in [Crossfade](https://developer.android.com/reference/kotlin/androidx/compose/animation/package-summary#Crossfade(kotlin.Any,androidx.compose.ui.Modifier,androidx.compose.animation.core.FiniteAnimationSpec,kotlin.Function1)) animation.
```kotlin
setContent {
    Navigator(HomeScreen) { navigator ->
        Crossfade(navigator.last) { screen ->
            screen.Content()
        }
    }
}
```

Want to use a custom animation? No problem, just follow the same principle.
```kotlin
setContent {
    Navigator(HomeScreen) { navigator ->
        MyCustomTransition {
            CurrentScreen()
        }
    }
}
```

### Tab navigation
Voyager provides a handy abstraction over the `Navigator` and `Screen`: the `TabNavigator` and `Tab`.

The `Tab` interface, like the `Screen`, has a `Content()` function, but also a `title` and an optional `icon`. Since tabs aren't usually reused, its OK to create them as `object`.
```kotlin
object HomeTab : Tab {

    override val title: String
        @Composable get() = stringResource(R.string.home)

    override val icon: Painter
        @Composable get() = rememberVectorPainter(Icons.Default.Home)

    @Composable
    override fun Content() {
        // ...
    }
}
```

The `TabNavigator` unlike the `Navigator`: 
* Don't handle back presses, because the tabs are siblings
* Don't exposes the [Stack API](#stack-api), just a `current` property

You can use it with a [Scaffold](https://developer.android.com/reference/kotlin/androidx/compose/material/package-summary#Scaffold(androidx.compose.ui.Modifier,androidx.compose.material.ScaffoldState,kotlin.Function0,kotlin.Function0,kotlin.Function1,kotlin.Function0,androidx.compose.material.FabPosition,kotlin.Boolean,kotlin.Function1,kotlin.Boolean,androidx.compose.ui.graphics.Shape,androidx.compose.ui.unit.Dp,androidx.compose.ui.graphics.Color,androidx.compose.ui.graphics.Color,androidx.compose.ui.graphics.Color,androidx.compose.ui.graphics.Color,androidx.compose.ui.graphics.Color,kotlin.Function1)) to easily create the UI for your tabs.
```kotlin
setContent {
    TabNavigator(HomeTab) {
        Scaffold(
            content = { 
                CurrentTab() 
            },
            bottomBar = {
                BottomNavigation {
                    TabNavigationItem(HomeTab)
                    TabNavigationItem(FavoritesTab)
                    TabNavigationItem(ProfileTab)
                }
            }
        )
    }
}
```

Use the `LocalTabNavigator` to get the current `TabNavigator`, and `current` to get and set the current tab.
```kotlin
@Composable
private fun RowScope.TabNavigationItem(tab: Tab) {
    val tabNavigator = LocalTabNavigator.current

    BottomNavigationItem(
        selected = tabNavigator.current == tab,
        onClick = { tabNavigator.current = tab },
        icon = { Icon(painter = tab.icon, contentDescription = tab.title) }
    )
}
```

### Nested navigation

For more complex use cases, when each tab should have its own independent navigation, like the [Youtube app](https://play.google.com/store/apps/details?id=com.google.android.youtube), you can combine the `TabNavigator` with multiple `Navigator`s.

Let's go back to the [previous example](#tab-navigation).
```kotlin
setContent {
    TabNavigator(HomeTab) {
        // ...
    }
}
```

But now, the `HomeTab` will have it's own `Navigator`.
```kotlin
object HomeTab : Screen {

    @Composable
    override fun Content() {
        Navigator(PostListScreen())
    }
}
```

That way, we can use the `LocalNavigator` to navigate deeper into `HomeTab`, or the `LocalTabNavigator` to switch between tabs.
```kotlin
class PostListScreen : Screen {

    @Composable
    private fun GoToPostDetailsScreenButton(post: Post) {
        val navigator = LocalNavigator.currentOrThrow
        
        Button(
            onClick = { navigator.push(PostDetailsScreen(post.id)) }
        )
    }

    @Composable
    private fun GoToProfileTabButton() {
        val tabNavigator = LocalTabNavigator.current

        Button(
            onClick = { tabNavigator.current = ProfileTab }
        )
    }
}
```

Going a little further, it's possible to have nested navigators. The `Navigator` has a `level` property (so you can check how deeper your are) and can have a `parent` navigator.
```kotlin
setContent {
    Navigator(ScreenA) { navigator0 ->
        println(navigator.level)
        // 0
        println(navigator.parent == null)
        // true
        Navigator(ScreenB) { navigator1 ->
            println(navigator.level)
            // 1
            println(navigator.parent == navigator0)
            // true
            Navigator(ScreenC) { navigator2 ->
                println(navigator.level)
                // 2
                println(navigator.parent == navigator1)
                // true
            }
        }
    }
}
```

Another operation is the `popUntilRoot()`, it will recursively pop all screens starting from the leaf navigator until the root one.

## Credits
* Logo by [Icons8](https://icons8.com/icon/SUYSVQr61Q6V/uss-voyager)