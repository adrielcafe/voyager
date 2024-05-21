# RxJava integration

!!! success
    To use the `disposables` and `RxScreenModel` you should first import `cafe.adriel.voyager:voyager-rxjava` (see [Setup](../setup.md)).

### CompositeDisposable

The `ScreenModel` provides a `disposables` property. It's cleared automatically when the `ScreenModel` is disposed.

```kotlin
class PostDetailsScreenModel(
    private val repository: PostRepository
) : ScreenModel {

    fun getPost(id: String) {
        repository.getPost(id)
            .subscribe { post -> /* ... */ }
            .let(disposables::add)
    }
}
```

### State-aware ScreenModel

If your `ScreenModel` needs to provide a state, use the `RxScreenModel`. Use `mutableState` to change the current state.

```kotlin
class PostDetailsScreenModel(
    private val repository: PostRepository
) : RxScreenModel<PostDetailsScreenModel.State>() {

    sealed class State {
        object Init : State()
        object Loading : State()
        data class Result(val post: Post) : State()
    }

    fun getPost(id: String) {
        repository.getPost(id)
            .doOnSubscribe { mutableState.onNext(State.Loading) }
            .subscribe { post -> mutableState.onNext(State.Result(post)) }
            .let(disposables::add)
    }
}
```

In your screen use `state.subscribeAsState()` and handle the current state.

```kotlin
class PostDetailsScreen : Screen {

    @Composable
    override fun Content() {
        val screenModel = rememberScreenModel<PostDetailsScreenModel>()
        val state by screenModel.state.subscribeAsState(initial = State.Init)

        when (state) {
            is State.Loading -> LoadingContent()
            is State.Result -> PostContent(state.post)
        }
    }
}
```

### Sample

!!! info
    Sample code [here](https://github.com/adrielcafe/voyager/tree/main/samples/android/src/main/java/cafe/adriel/voyager/sample/rxjavaIntegration).
