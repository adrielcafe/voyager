package cafe.adriel.voyager.kodein

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import org.kodein.di.compose.localDI
import org.kodein.di.direct
import org.kodein.di.provider

@Composable
public inline fun <reified T : ScreenModel> Screen.rememberScreenModel(
    tag: Any? = null
): T = with(localDI()) {
    rememberScreenModel(tag = tag?.toString()) { direct.provider<T>(tag)() }
}

@Composable
public inline fun <reified A : Any, reified T : ScreenModel> Screen.rememberScreenModel(
    tag: Any? = null,
    arg: A
): T = with(localDI()) {
    rememberScreenModel(tag = tag?.toString()) { direct.provider<A, T>(tag, arg)() }
}
