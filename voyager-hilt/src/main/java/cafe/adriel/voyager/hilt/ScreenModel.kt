package cafe.adriel.voyager.hilt

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.hilt.internal.componentActivity
import dagger.hilt.android.EntryPointAccessors

/**
 * Provide a [ScreenModel] getting from Hilt graph.
 *
 * @return A new instance of [ScreenModel] or the same instance remembered by the composition
 */
@Composable
public inline fun <reified T : ScreenModel> Screen.getScreenModel(): T {
    val context = LocalContext.current
    return rememberScreenModel {
        val screenModels = EntryPointAccessors
            .fromActivity(context.componentActivity, ScreenModelEntryPoint::class.java)
            .screenModels()
        val model = screenModels[T::class.java]?.get()
            ?: error("${T::class} screen model not found in hilt graph")
        model as T
    }
}

/**
 * Provide a [ScreenModel] using a custom [ScreenModelFactory]. The [ScreenModelFactory] is provided by Hilt graph.
 *
 * @param factory A function that receives a [ScreenModelFactory] and returns a [ScreenModel] created by the custom factory
 * @return A new instance of [ScreenModel] or the same instance remembered by the composition
 */
@Composable
public inline fun <reified T : ScreenModel, reified F : ScreenModelFactory> Screen.getScreenModel(
    noinline factory: (F) -> T
): T {
    val context = LocalContext.current
    return rememberScreenModel {
        val screenFactories = EntryPointAccessors
            .fromActivity(context.componentActivity, ScreenModelEntryPoint::class.java)
            .screenModelFactories()
        val screenFactory = screenFactories[F::class.java]?.get()
            ?: error("${F::class} screen model factory not found in hilt graph")
        factory.invoke(screenFactory as F)
    }
}
