package cafe.adriel.voyager.kodein

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.annotation.ExperimentalVoyagerApi
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.screenModel.rememberNavigatorScreenModel
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

@ExperimentalVoyagerApi
@Composable
public inline fun <reified T : ScreenModel> Navigator.rememberNavigatorScreenModel(
    tag: Any? = null
): T = with(localDI()) {
    rememberNavigatorScreenModel(tag = tag?.toString()) { direct.provider<T>(tag)() }
}

@ExperimentalVoyagerApi
@Composable
public inline fun <reified A : Any, reified T : ScreenModel> Navigator.rememberNavigatorScreenModel(
    tag: Any? = null,
    arg: A
): T = with(localDI()) {
    rememberNavigatorScreenModel(tag = tag?.toString()) { direct.provider<A, T>(tag, arg)() }
}
