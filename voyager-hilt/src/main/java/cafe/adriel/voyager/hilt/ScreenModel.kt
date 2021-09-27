package cafe.adriel.voyager.hilt

import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import cafe.adriel.voyager.androidx.AndroidScreen
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.rememberScreenModel
import dagger.hilt.android.EntryPointAccessors

/**
 * Provide a screen model getting from Hilt graph.
 */
@Composable
public inline fun <reified T : ScreenModel> AndroidScreen.getScreenModel(): T {
    val context = LocalContext.current
    return rememberScreenModel(tag = T::class.qualifiedName) {
        val componentActivity = context as? ComponentActivity
            ?: throw IllegalStateException("Invalid local context on AndroidScreen. It must be a ComponentActivity")
        val screenModels = EntryPointAccessors
            .fromActivity(componentActivity, ScreenModelEntryPoint::class.java)
            .screenModels()
        val model = screenModels[T::class.java]?.get()
            ?: throw IllegalStateException("${T::class} screen model not found in hilt graph")
        return@rememberScreenModel model as T
    }
}

/**
 * Provide a screen model getting from Hilt graph using a custom factory to create an instance
 *
 * @param factory A function that receives a screen factory and returns a screen model created using the custom factory
 */
@Composable
public inline fun <reified T : ScreenModel, reified F> AndroidScreen.getScreenModel(
    noinline factory: (F) -> T
): T where F : ScreenFactory {
    val context = LocalContext.current
    return rememberScreenModel(tag = T::class.qualifiedName) {
        val componentActivity = context as? ComponentActivity
            ?: throw IllegalStateException("Invalid local context on AndroidScreen. It must be a ComponentActivity")
        val screenFactories = EntryPointAccessors
            .fromActivity(componentActivity, ScreenModelEntryPoint::class.java)
            .screenFactories()
        val screenFactory = screenFactories[F::class.java]?.get()
            ?: throw IllegalStateException("${F::class} screen model factory not found in hilt graph")
        return@rememberScreenModel factory.invoke(screenFactory as F)
    }
}
