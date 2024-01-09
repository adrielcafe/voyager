package cafe.adriel.voyager.androidx

import android.app.Application
import android.content.Context
import android.content.ContextWrapper
import androidx.activity.ComponentActivity

public inline fun <reified T> Context.findOwner(
    noinline nextFunction: (Context) -> Context? = { (it as? ContextWrapper)?.baseContext }
): T? = generateSequence(seed = this, nextFunction = nextFunction).mapNotNull { context ->
    context as? T
}.firstOrNull()

public val Context.application: Application?
    get() = findOwner<Application> { it.applicationContext }

public val Context.componentActivity: ComponentActivity?
    get() = findOwner<ComponentActivity>()
