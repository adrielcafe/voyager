# BottomSheet navigation

!!! success
    To use the `BottomSheetNavigator` you should first import `cafe.adriel.voyager:voyager-bottom-sheet-navigator` (see [Setup](../setup.md)).

Voyager provides a specialized navigator for `ModalBottomSheetLayout`: the `BottomSheetNavigator`.

Call it and set the **back layer** content. The BottomSheet content (or the **front layer**) will be set on demand.

```kotlin
setContent {
    BottomSheetNavigator {
        BackContent()
    }
}
```

You can also use the default `Navigator` to navigate on your back layer content.

```kotlin
setContent {
    BottomSheetNavigator {
        Navigator(BackScreen())
    }
}
```

The `BottomSheetNavigator` accepts the same params as `ModalBottomSheetLayout`.

```kotlin
@Composable
public fun BottomSheetNavigator(
    modifier: Modifier = Modifier,
    hideOnBackPress: Boolean = true,
    scrimColor: Color = ModalBottomSheetDefaults.scrimColor,
    sheetShape: Shape = MaterialTheme.shapes.large,
    sheetElevation: Dp = ModalBottomSheetDefaults.Elevation,
    sheetBackgroundColor: Color = MaterialTheme.colors.surface,
    sheetContentColor: Color = contentColorFor(sheetBackgroundColor),
    // ...
)
```

Use the `LocalBottomSheetNavigator` to show and hide the BottomSheet.

```kotlin
class BackScreen : Screen {

    @Composable
    override fun Content() {
        val bottomSheetNavigator = LocalBottomSheetNavigator.current

        Button(
            onClick = { 
                bottomSheetNavigator.show(FrontScreen())
            }
        ) {
            Text(text = "Show BottomSheet")
        }
    }
}
```

### Sample

![](../media/assets/navigation-bottom-sheet.gif)

!!! info
    Source code [here](https://github.com/adrielcafe/voyager/tree/main/samples/android/src/main/java/cafe/adriel/voyager/sample/bottomSheetNavigation).
