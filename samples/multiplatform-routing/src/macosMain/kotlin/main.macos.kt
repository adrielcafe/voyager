import androidx.compose.ui.window.Window
import cafe.adriel.voyager.sample.multiplatform.routing.SampleApplication
import platform.AppKit.NSApp
import platform.AppKit.NSApplication

fun main() {
    NSApplication.sharedApplication()
    Window("VoyagerMultiplatformRouting") {
        SampleApplication()
    }
    NSApp?.run()
}
