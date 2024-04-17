# Overview

## [Voyager](https://en.wikipedia.org/wiki/USS_Voyager_(Star_Trek)): Compose on Warp Speed

A multiplatform navigation library built for, and seamlessly integrated with, [Jetpack Compose](https://developer.android.com/jetpack/compose).

Create scalable Single-Activity apps powered by a [pragmatic API](navigation.md):

```kotlin
class HomeScreenModel : ScreenModel {
    // ...
}

class HomeScreen : Screen {

    @Composable
    override fun Content() {
        val screenModel = rememberScreenModel { HomeScreenModel() }
        // ...
    }
}

class SingleActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            Navigator(HomeScreen())
        }
    }
}
```

Turn on the Warp Drive and enjoy the voyage 🖖

### Features

- [Supported platforms](): Android, iOS, Desktop, Web
- [Linear navigation]()
- [BottomSheet navigation]()
- [Tab navigation]() like [Youtube app](https://play.google.com/store/apps/details?id=com.google.android.youtube)
- [Nested navigation]() (multiple stacks, parent navigation)
- [ScreenModel]() (a.k.a ViewModel) integrated with [Koin](), [Kodein](), [Hilt](), [Coroutines](), [RxJava](), [LiveData]()
- [Android ViewModel]() integration (with [Hilt support]())
- Type-safe [multi-module navigation]()
- State-aware [Stack API]()
- Built-in [transitions]()
- [State restoration]() after Activity recreation
- [Lifecycle]() callbacks
- [Back press]() handling
- [Deep linking]() support

### Roadmap
- Navigator scoped ViewModels
- Androidx ViewModel Multiplatform support
- Androidx Lifecycle Multiplatform support

### Credits

### - Logo by [Icons8](https://icons8.com/icon/SUYSVQr61Q6V/uss-voyager)

