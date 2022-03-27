import androidx.compose.ui.window.Window
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp
import platform.AppKit.NSApp
import platform.AppKit.NSApplication

fun main() {
    NSApplication.sharedApplication()
    Window("Falling Balls") {
        val game = remember { Game(MacosTime) }
        FallingBalls(game)
    }
    NSApp?.run()
}

