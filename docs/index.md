# Overview

![](./media/assets/Sem título-1.png)

### [Voyager](https://en.wikipedia.org/wiki/USS\_Voyager\_\(Star\_Trek\)): Compose on Warp Speed

A multiplatform navigation library built for, and seamlessly integrated with, [Jetpack Compose](https://developer.android.com/jetpack/compose).

Create scalable Single-Activity apps powered by a [pragmatic API](navigation/):

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

### **Features**

* [Supported platforms](setup.md#platform-compatibility): Android, iOS, Desktop, Web, Wasm (since 1.1.0-alpha03)
* [Linear navigation](navigation/)
* [BottomSheet navigation](navigation/bottomsheet-navigation.md)
* [Tab navigation](navigation/tab-navigation.md) like [Youtube app](https://play.google.com/store/apps/details?id=com.google.android.youtube)
* [Nested navigation](navigation/nested-navigation.md) (multiple stacks, parent navigation)
* [ScreenModel](screenmodel/) (a.k.a ViewModel) integrated with [Koin](screenmodel/koin-integration.md), [Kodein](screenmodel/kodein-integration.md), [Hilt](screenmodel/hilt-integration.md), [Coroutines](screenmodel/coroutines-integration.md), [RxJava](screenmodel/rxjava-integration.md), [LiveData](screenmodel/livedata-integration.md)
* [Android ViewModel](android-viewmodel/) integration (with [Hilt support](android-viewmodel/hilt-integration.md))
* Type-safe [multi-module navigation](navigation/multi-module-navigation.md)
* State-aware [Stack API](stack-api.md)
* Built-in [transitions](transitions-api)
* [State restoration](state-restoration.md) after Activity recreation
* [Lifecycle](lifecycle.md) callbacks
* [Back press](back-press.md) handling
* [Deep linking](deep-links.md) support
* [Lifecycle KMP support](android-viewmodel/viewmodel-kmp.md) since 1.1.0-beta01

### Credits

* Logo by [Icons8](https://icons8.com/icon/SUYSVQr61Q6V/uss-voyager)
