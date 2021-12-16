package cafe.adriel.voyager.hilt.internal

import android.content.Context
import android.content.ContextWrapper
import androidx.activity.ComponentActivity
import androidx.lifecycle.ViewModelProvider

// Unfortunately findOwner function is internal in activity-compose
// TODO: Maybe move to androidx module because we'll need this function when implement onCloseRequest support
internal inline fun <reified T> findOwner(context: Context): T? {
    var innerContext = context
    while (innerContext is ContextWrapper) {
        if (innerContext is T) {
            return innerContext
        }
        innerContext = innerContext.baseContext
    }
    return null
}

@PublishedApi
internal val Context.componentActivity: ComponentActivity
    get() = findOwner<ComponentActivity>(this)
        ?: error("Context must be a androidx.activity.ComponentActivity. Current is $this")

@PublishedApi
internal val Context.defaultViewModelProviderFactory: ViewModelProvider.Factory
    get() = componentActivity.defaultViewModelProviderFactory
