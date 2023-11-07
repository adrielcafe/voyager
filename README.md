[![Maven metadata URL](https://img.shields.io/maven-metadata/v?color=blue&metadataUrl=https://s01.oss.sonatype.org/service/local/repo_groups/public/content/cafe/adriel/voyager/voyager-core/maven-metadata.xml&style=for-the-badge)](https://repo.maven.apache.org/maven2/cafe/adriel/voyager/)
[![Android API](https://img.shields.io/badge/api-21%2B-brightgreen.svg?style=for-the-badge)](https://android-arsenal.com/api?level=21)
[![kotlin](https://img.shields.io/github/languages/top/adrielcafe/voyager.svg?style=for-the-badge&color=blueviolet)](https://kotlinlang.org/)
[![ktlint](https://img.shields.io/badge/code%20style-%E2%9D%A4-FF4081.svg?style=for-the-badge)](https://ktlint.github.io/)
[![License MIT](https://img.shields.io/github/license/adrielcafe/voyager.svg?style=for-the-badge&color=orange)](https://opensource.org/licenses/MIT)

<h1 align="center">
    <img height="150" src="https://user-images.githubusercontent.com/2512298/127723355-f56b3040-47cb-44fd-8504-a1868721c1a3.png"/>
    <br>
    <a href="https://voyager.adriel.cafe">Voyager</a>: Compose on Warp Speed
</h1>

A multiplatform navigation library built for, and seamlessly integrated with, [Jetpack Compose](https://developer.android.com/jetpack/compose).

Create scalable Single-Activity apps powered by a [pragmatic API](https://voyager.adriel.cafe/navigation):

```kotlin
class HomeScreenModel : ScreenModel {
    // ...
}

class HomeScreen : Screen {

    @Composable
    override fun Content() {
        val screenModel = rememberScreenModel<HomeScreenModel>()
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

### Documentation
See the [project website](https://voyager.adriel.cafe) for documentation and APIs.

### Features
- [Supported platforms](https://voyager.adriel.cafe/setup#platform-compatibility): Android, iOS, Desktop, Web.
- [Linear navigation](https://voyager.adriel.cafe/navigation)
- [BottomSheet navigation](https://voyager.adriel.cafe/navigation/bottomsheet-navigation)
- [Tab navigation](https://voyager.adriel.cafe/navigation/tab-navigation) like [Youtube app](https://play.google.com/store/apps/details?id=com.google.android.youtube)
- [Nested navigation](https://voyager.adriel.cafe/navigation/nested-navigation) (multiple stacks, parent navigation)
- [ScreenModel](https://voyager.adriel.cafe/screenmodel) (a.k.a ViewModel) integrated with [Koin](https://voyager.adriel.cafe/screenmodel/koin-integration), [Kodein](https://voyager.adriel.cafe/screenmodel/kodein-integration), [Hilt](https://voyager.adriel.cafe/screenmodel/hilt-integration), [Coroutines](https://voyager.adriel.cafe/screenmodel/coroutines-integration), [RxJava](https://voyager.adriel.cafe/screenmodel/rxjava-integration), [LiveData](https://voyager.adriel.cafe/screenmodel/livedata-integration)
- [Android ViewModel](https://voyager.adriel.cafe/android-viewmodel) integration (with [Hilt support](https://voyager.adriel.cafe/android-viewmodel/hilt-integration))
- Type-safe [multi-module navigation](https://voyager.adriel.cafe/navigation/multi-module-navigation)
- State-aware [Stack API](https://voyager.adriel.cafe/stack-api)
- Built-in [transitions](https://voyager.adriel.cafe/transitions)
- [State restoration](https://voyager.adriel.cafe/state-restoration) after Activity recreation
- [Lifecycle](https://voyager.adriel.cafe/lifecycle) callbacks
- [Back press](https://voyager.adriel.cafe/back-press) handling
- [Deep linking](https://voyager.adriel.cafe/deep-links) support

### Samples
| [Stack API](https://github.com/adrielcafe/voyager/tree/main/samples/android/src/main/java/cafe/adriel/voyager/sample/stateStack) | [Android ViewModel](https://github.com/adrielcafe/voyager/tree/main/samples/android/src/main/java/cafe/adriel/voyager/sample/androidViewModel) | [ScreenModel](https://github.com/adrielcafe/voyager/tree/main/samples/android/src/main/java/cafe/adriel/voyager/sample/screenModel) | [Basic nav.](https://github.com/adrielcafe/voyager/tree/main/samples/android/src/main/java/cafe/adriel/voyager/sample/basicNavigation) |
|----------|----------|----------|----------|
| ![navigation-stack](https://user-images.githubusercontent.com/2512298/126323192-9b6349fe-7b96-4acf-b62e-c75165d909e1.gif) | ![navigation-android-viewmodel](https://user-images.githubusercontent.com/2512298/130377801-c350b4f5-bcca-4d28-9403-0d9d4c1e99f7.gif) | ![navigation-screenmodel](https://user-images.githubusercontent.com/2512298/131770829-fa85cb19-cc76-4fbf-9bdc-165997d5349d.gif) | ![navigation-basic](https://user-images.githubusercontent.com/2512298/126323165-47760eec-2ba2-48ee-8e3a-841d50098d33.gif) |

| [BottomSheet nav.](https://github.com/adrielcafe/voyager/tree/main/samples/android/src/main/java/cafe/adriel/voyager/sample/bottomSheetNavigation) | [Tab nav.](https://github.com/adrielcafe/voyager/tree/main/samples/android/src/main/java/cafe/adriel/voyager/sample/tabNavigation) | [Multi-module nav.](https://github.com/adrielcafe/voyager/tree/main/samples/multi-module) | [Nested nav.](https://github.com/adrielcafe/voyager/tree/main/samples/android/src/main/java/cafe/adriel/voyager/sample/nestedNavigation) |
|----------|----------|----------|----------|
| ![navigation-bottom-sheet](https://user-images.githubusercontent.com/2512298/131191122-18025192-ce4d-4659-9afa-aacfdb488796.gif) | ![navigation-tab](https://user-images.githubusercontent.com/2512298/126323588-2f970953-0adb-47f8-b2fb-91c5854656bd.gif) | ![navigation-multi-module](https://user-images.githubusercontent.com/2512298/130662717-c15caf88-350e-42a0-837c-3453805b68f2.gif) | ![navigation-nested](https://user-images.githubusercontent.com/2512298/126323027-a2633aef-9402-4df8-9384-45935d7986cf.gif) |
