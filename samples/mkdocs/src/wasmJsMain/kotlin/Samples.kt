import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import cafe.adriel.voyager.sample.shared.basicNavigation.BasicNavigationSample
import cafe.adriel.voyager.sample.shared.bottomSheetNavigation.BottomSheetNavigationSample
import cafe.adriel.voyager.sample.shared.lifecycleViewModel.LifecycleViewModelSample
import cafe.adriel.voyager.sample.shared.nestedNavigation.NestedNavigationSample
import cafe.adriel.voyager.sample.shared.screenModel.ScreenModelSample
import cafe.adriel.voyager.sample.shared.tabNavigation.TabNavigationSample

@OptIn(ExperimentalComposeUiApi::class, ExperimentalJsExport::class)
@JsExport
fun basicNavigationSample(containerId: String) {
    ComposeViewport(viewportContainerId = containerId) {
        BasicNavigationSample()
    }
}

@OptIn(ExperimentalComposeUiApi::class, ExperimentalJsExport::class)
@JsExport
fun bottomSheetNavigationSample(containerId: String) {
    ComposeViewport(viewportContainerId = containerId) {
        BottomSheetNavigationSample()
    }
}

@OptIn(ExperimentalComposeUiApi::class, ExperimentalJsExport::class)
@JsExport
fun lifecycleViewModelSample(containerId: String) {
    ComposeViewport(viewportContainerId = containerId) {
        LifecycleViewModelSample()
    }
}

@OptIn(ExperimentalComposeUiApi::class, ExperimentalJsExport::class)
@JsExport
fun nestedNavigationSample(containerId: String) {
    ComposeViewport(viewportContainerId = containerId) {
        NestedNavigationSample()
    }
}

@OptIn(ExperimentalComposeUiApi::class, ExperimentalJsExport::class)
@JsExport
fun screenModelSample(containerId: String) {
    ComposeViewport(viewportContainerId = containerId) {
        ScreenModelSample()
    }
}

@OptIn(ExperimentalComposeUiApi::class, ExperimentalJsExport::class)
@JsExport
fun tabNavigationSample(containerId: String) {
    ComposeViewport(viewportContainerId = containerId) {
        TabNavigationSample()
    }
}
