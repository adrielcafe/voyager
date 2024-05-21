# LiveData integration

!!! success
    To use the `LiveScreenModel` you should first import `cafe.adriel.voyager:voyager-livedata` (see [Setup](../setup.md)).

### State-aware ScreenModel

If your `ScreenModel` needs to provide a state, use the `LiveScreenModel`. Set the initial state on the constructor and use `mutableState` to change the current state.

```kotlin
class PostDetailsScreenModel(
    private val repository: PostRepository
) : LiveScreenModel<PostDetailsScreenModel.State>(State.Init) {

    sealed class State {
        object Init : State()
        object Loading : State()
        data class Result(val post: Post) : State()
    }

    fun getPost(id: String) {
        coroutineScope.launch {
            val result = State.Result(post = repository.getPost(id))
            mutableState.postValue(result)
        }
    }
}
```

In your screen use `state.observeAsState()` and handle the current state.

```kotlin
class PostDetailsScreen : Screen {

    @Composable
    override fun Content() {
        val screenModel = rememberScreenModel<PostDetailsScreenModel>()
        val state by screenModel.state.observeAsState()

        when (state) {
            is State.Loading -> LoadingContent()
            is State.Result -> PostContent(state.post)
        }
    }
}
```

### Sample

!!! info
    Sample code [here](https://github.com/adrielcafe/voyager/tree/main/samples/android/src/main/java/cafe/adriel/voyager/sample/liveDataIntegration).
