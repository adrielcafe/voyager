package cafe.adriel.voyager.koin

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.rememberNavigatorScreenModel
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.Navigator
import org.koin.compose.currentKoinScope
import org.koin.compose.stable.rememberStableParametersDefinition
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.qualifier.Qualifier
import org.koin.core.scope.Scope

@Composable
public inline fun <reified T : ScreenModel> Screen.koinScreenModel(
    qualifier: Qualifier? = null,
    scope: Scope = currentKoinScope(),
    noinline parameters: ParametersDefinition? = null
): T {
    val st = parameters?.let { rememberStableParametersDefinition(parameters) }
    val tag = remember(qualifier, scope) { qualifier?.value }
    return rememberScreenModel(tag = tag) {
        scope.get(qualifier, st?.parametersDefinition)
    }
}

@Composable
public inline fun <reified T : ScreenModel> Navigator.koinNavigatorScreenModel(
    qualifier: Qualifier? = null,
    scope: Scope = currentKoinScope(),
    noinline parameters: ParametersDefinition? = null
): T {
    val st = parameters?.let { rememberStableParametersDefinition(parameters) }
    val tag = remember(qualifier, scope) { qualifier?.value }
    return rememberNavigatorScreenModel(tag = tag) {
        scope.get(qualifier, st?.parametersDefinition)
    }
}

@Deprecated(
    message = "use koinScreenModel() instead. Will be removed on 1.1.0",
    replaceWith = ReplaceWith("koinScreenModel")
)
@Composable
public inline fun <reified T : ScreenModel> Screen.getScreenModel(
    qualifier: Qualifier? = null,
    noinline parameters: ParametersDefinition? = null
): T = koinScreenModel<T>(qualifier = qualifier, parameters = parameters)

@Deprecated(
    message = "use koinNavigatorScreenModel() instead. Will be removed on 1.1.0",
    replaceWith = ReplaceWith("koinNavigatorScreenModel")
)
@Composable
public inline fun <reified T : ScreenModel> Navigator.getNavigatorScreenModel(
    qualifier: Qualifier? = null,
    noinline parameters: ParametersDefinition? = null
): T = koinNavigatorScreenModel(qualifier = qualifier, parameters = parameters)
