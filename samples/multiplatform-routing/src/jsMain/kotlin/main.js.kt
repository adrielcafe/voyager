import androidx.compose.ui.window.Window
import cafe.adriel.voyager.sample.multiplatform.routing.SampleApplication
import org.jetbrains.skiko.wasm.onWasmReady

fun main() {
    onWasmReady {
        Window("Voyager Multiplatform Routing") {
            SampleApplication()
        }
    }
}
