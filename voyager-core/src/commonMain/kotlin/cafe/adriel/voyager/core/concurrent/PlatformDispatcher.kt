package cafe.adriel.voyager.core.concurrent

import cafe.adriel.voyager.core.annotation.InternalVoyagerApi
import kotlinx.coroutines.CoroutineDispatcher

@InternalVoyagerApi
public expect val PlatformMainDispatcher: CoroutineDispatcher
