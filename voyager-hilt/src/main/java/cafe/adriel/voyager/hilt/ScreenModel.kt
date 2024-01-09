package cafe.adriel.voyager.hilt

import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import cafe.adriel.voyager.androidx.componentActivity
import cafe.adriel.voyager.core.annotation.InternalVoyagerApi
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.rememberNavigatorScreenModel
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.Navigator
import dagger.hilt.android.EntryPointAccessors

@Composable
@PublishedApi
@InternalVoyagerApi
internal fun getActivity(): ComponentActivity {
    val context = LocalContext.current
    return remember(context) {
        checkNotNull(context.componentActivity) {
            "No androidx.activity.ComponentActivity found in the context: $context"
        }
    }
}

@PublishedApi
@InternalVoyagerApi
internal inline fun <reified T : ScreenModel> createScreenModel(
    activity: ComponentActivity
): T {
    val screenModels = EntryPointAccessors
        .fromActivity(activity, ScreenModelEntryPoint::class.java)
        .screenModels()
    val model = screenModels[T::class.java]?.get()
        ?: error(
            "${T::class.java} not found in hilt graph.\nPlease, check if you have a Multibinding " +
                "declaration to your ScreenModel using @IntoMap and " +
                "@ScreenModelKey(${T::class.qualifiedName}::class)"
        )
    return model as T
}

@PublishedApi
@InternalVoyagerApi
internal inline fun <reified T : ScreenModel, reified F : ScreenModelFactory> createScreenModelUsingFactory(
    activity: ComponentActivity,
    noinline factory: (F) -> T
): T {
    val screenFactories = EntryPointAccessors
        .fromActivity(activity, ScreenModelEntryPoint::class.java)
        .screenModelFactories()
    val screenFactory = screenFactories[F::class.java]?.get()
        ?: error(
            "${F::class.java} not found in hilt graph.\nPlease, check if you have a Multibinding " +
                "declaration to your ScreenModelFactory using @IntoMap and " +
                "@ScreenModelFactoryKey(${F::class.qualifiedName}::class)"
        )
    return factory.invoke(screenFactory as F)
}

/**
 * Provide a [ScreenModel] getting from Hilt graph.
 *
 * @return A new instance of [ScreenModel] or the same instance remembered by the composition
 */
@Composable
public inline fun <reified T : ScreenModel> Screen.getScreenModel(
    tag: String? = null
): T {
    val activity = getActivity()
    return rememberScreenModel(tag = tag) {
        createScreenModel(activity)
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
    tag: String? = null,
    noinline factory: (F) -> T
): T {
    val activity = getActivity()
    return rememberScreenModel(tag = tag) {
        createScreenModelUsingFactory<T, F>(
            activity = activity,
            factory = factory
        )
    }
}

/**
 * Provide a [ScreenModel] getting from Hilt graph, lifecycle bounded to the Navigator.
 *
 * @return A new instance of [ScreenModel] or the same instance remembered by the composition
 */
@Composable
public inline fun <reified T : ScreenModel> Navigator.getNavigatorScreenModel(
    tag: String? = null
): T {
    val activity = getActivity()
    return rememberNavigatorScreenModel(tag = tag) {
        createScreenModel(activity = activity)
    }
}

/**
 * Provide a [ScreenModel] using a custom [ScreenModelFactory], lifecycle bounded to the Navigator.
 * The [ScreenModelFactory] is provided by Hilt graph.
 *
 * @param factory A function that receives a [ScreenModelFactory] and returns a [ScreenModel] created by the custom factory
 * @return A new instance of [ScreenModel] or the same instance remembered by the composition
 */
@Composable
public inline fun <reified T : ScreenModel, reified F : ScreenModelFactory> Navigator.getNavigatorScreenModel(
    tag: String? = null,
    noinline factory: (F) -> T
): T {
    val activity = getActivity()
    return rememberNavigatorScreenModel(tag = tag) {
        createScreenModelUsingFactory<T, F>(
            activity = activity,
            factory = factory
        )
    }
}
