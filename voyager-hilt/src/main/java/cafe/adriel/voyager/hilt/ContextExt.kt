package cafe.adriel.voyager.hilt

import android.content.Context
import androidx.activity.ComponentActivity
import androidx.lifecycle.ViewModelProvider

public val Context.componentActivity: ComponentActivity
    get() = this as? ComponentActivity
        ?: error("Invalid local context. It must be a ComponentActivity")

public val Context.defaultViewModelProviderFactory: ViewModelProvider.Factory
    get() = componentActivity.defaultViewModelProviderFactory
