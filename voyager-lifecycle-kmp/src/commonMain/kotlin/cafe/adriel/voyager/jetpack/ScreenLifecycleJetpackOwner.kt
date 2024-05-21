package cafe.adriel.voyager.jetpack

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import cafe.adriel.voyager.core.annotation.ExperimentalVoyagerApi
import cafe.adriel.voyager.core.annotation.InternalVoyagerApi
import cafe.adriel.voyager.core.lifecycle.ScreenLifecycleOwner
import cafe.adriel.voyager.core.screen.Screen

@InternalVoyagerApi
@ExperimentalVoyagerApi
public class ScreenLifecycleKMPOwner : ScreenLifecycleOwner {
    public val owner: VoyagerLifecycleKMPOwner = VoyagerLifecycleKMPOwner()

    @Composable
    override fun ProvideBeforeScreenContent(
        provideSaveableState: @Composable (suffixKey: String, content: @Composable () -> Unit) -> Unit,
        content: @Composable () -> Unit
    ) {
        provideSaveableState("lifecycle") {
            owner.LifecycleDisposableEffect()

            val hooks = owner.getHooks()

            CompositionLocalProvider(*hooks.toTypedArray()) {
                content()
            }
        }
    }

    override fun onDispose(screen: Screen) {
        owner.onDispose()
    }
}
