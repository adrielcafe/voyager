package cafe.adriel.voyager.hilt.internal

import android.content.Context
import androidx.activity.ComponentActivity

@PublishedApi
internal val Context.componentActivity: ComponentActivity
    get() = this as? ComponentActivity
        ?: error("Invalid local context. It must be a ComponentActivity")
