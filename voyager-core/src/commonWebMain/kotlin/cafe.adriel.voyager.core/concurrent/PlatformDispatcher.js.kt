package cafe.adriel.voyager.core.concurrent

import cafe.adriel.voyager.core.annotation.InternalVoyagerApi
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

@InternalVoyagerApi
public actual val PlatformMainDispatcher: CoroutineDispatcher
    get() = Dispatchers.Default
