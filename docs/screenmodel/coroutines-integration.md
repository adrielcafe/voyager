# Coroutines integration

!!! success
    The `screenModelScope` and `StateScreenModel`are part of the core library.

### CoroutineScope

The `ScreenModel` provides a `screenModelScope` property. It's canceled automatically when the `ScreenModel` is disposed.

```kotlin
class PostDetailsScreenModel(
    private val repository: PostRepository
) : ScreenModel {

    fun getPost(id: String) {
        screenModelScope.launch {
            val post = repository.getPost(id)
            // ...
        }
    }
}
```

### State-aware ScreenModel

If your `ScreenModel` needs to provide a state, use the `StateScreenModel`. Set the initial state on the constructor and use `mutableState` to change the current state.

```kotlin
class PostDetailsScreenModel(
    private val repository: PostRepository
) : StateScreenModel<PostDetailsScreenModel.State>(State.Init) {

    sealed class State {
        object Init : State()
        object Loading : State()
        data class Result(val post: Post) : State()
    }

    fun getPost(id: String) {
        coroutineScope.launch {
            mutableState.value = State.Loading
            mutableState.value = State.Result(post = repository.getPost(id))
        }
    }
}
```

In your screen use `state.collectAsState()` and handle the current state.

```kotlin
class PostDetailsScreen : Screen {

    @Composable
    override fun Content() {
        val screenModel = rememberScreenModel<PostDetailsScreenModel>()
        val state by screenModel.state.collectAsState()

        when (state) {
            is State.Loading -> LoadingContent()
            is State.Result -> PostContent(state.post)
        }
    }
}
```

### Sample

!!! info
    Sample code [here](https://github.com/adrielcafe/voyager/tree/main/samples/android/src/main/java/cafe/adriel/voyager/sample/screenModel).

### Desktop Note

!!! info
    If you are targeting Desktop, you should provide the dependency `org.jetbrains.kotlinx:kotlinx-coroutines-swing`, the `screenModelScope` depends on `Dispatchers.Main` provided by this library on Desktop. We don't include it because this library is incompatible with IntelliJ Plugin, [see](https://youtrack.jetbrains.com/issue/IDEA-285839). If you are targeting Desktop for IntelliJ plugins, this library does not require to be provided.