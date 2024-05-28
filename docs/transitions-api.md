# Transitions

!!! success ""
    To use the transitions you should first import `cafe.adriel.voyager:voyager-transitions` (see [Setup](setup.md)).

Voyager has built-in transitions! When initializing the `Navigator` you can override the default content and use, for example, the `SlideTransition`.

```kotlin
setContent {
    Navigator(HomeScreen) { navigator ->
        SlideTransition(navigator)
    }
}
```

!!! warning
    Have encounter `Screen was used multiple times` crash? Provide a `uniqueScreenKey` for your Screens

    ```kotlin hl_lines="3 4"
    class ScreenFoo : Screen {
        
        override val key: ScreenKey
            get() = uniqueScreenKey
    
        @Composable
        override fun Content() {
            ...
        }
    ```

### **Available transitions**

|      `FadeTransition()`      |      `SlideTransition()`      |
|:----------------------------:|:-----------------------------:|
| ![](./media/assets/fade.gif) | ![](./media/assets/slide.gif) |

|      `ScaleTransition()`      |
|:-----------------------------:|
| ![](./media/assets/scale.gif) |

### Custom transitions

It's simple to add your own transitions: call `ScreenTransition` with a custom `transitionModifier`. Use the available params (`screen`, `transition` and `event`) to animate as needed.

```kotlin
@Composable
fun MyCustomTransition(
    navigator: Navigator,
    modifier: Modifier = Modifier,
    content: ScreenTransitionContent
) {
    ScreenTransition(
        navigator = navigator,
        modifier = modifier,
        content = content,
        transition = {
            val (initialScale, targetScale) = when (navigator.lastEvent) {
                StackEvent.Pop -> ExitScales
                else -> EnterScales
            }

            scaleIn(initialScale) with scaleOut(targetScale)
        }
    )
}

setContent {
    Navigator(HomeScreen) { navigator ->
        MyCustomTransition(navigator) { screen ->
            screen.Content()
        }
    }
}
```

!!! info ""
    Take a look at the [source of the available transitions](https://github.com/adrielcafe/voyager/tree/main/voyager-transitions/src/commonMain/kotlin/cafe/adriel/voyager/transitions) for working examples.

### Per Screen transitions [Experimental]

If you want to define a Enter and Exit transition for a specific Screen, you have a lot of options to do
starting from 1.1.0-beta01 Voyager have a new experimental API for this purpose.

```kotlin
class ExampleScaleScreen : Screen, ScreenTransition {
    override val key: ScreenKey
        get() = uniqueScreenKey

    @Composable
    override fun Content() {
        ...
    }
    
    override fun enter(): EnterTransition? = scaleIn()

    override fun exit(): ExitTransition? = scaleOut()
}
```

The API works with any ScreenTransition API, you just need to provide one and the Per Screen transition should.
```kotlin
setContent {
    Navigator(HomeScreen) { navigator ->
        SlideTransition(navigator)
    }
}
```

!!! warning ""
    `CrossfadeTransition` is not supported yet.