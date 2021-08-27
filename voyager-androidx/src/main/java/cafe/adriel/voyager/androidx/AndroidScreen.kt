package cafe.adriel.voyager.androidx

import cafe.adriel.voyager.core.lifecycle.ScreenLifecycleOwner
import cafe.adriel.voyager.core.lifecycle.ScreenLifecycleProvider
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import cafe.adriel.voyager.core.screen.uniqueScreenKey

public abstract class AndroidScreen : Screen, ScreenLifecycleProvider {

    override val key: ScreenKey = uniqueScreenKey

    override fun getLifecycleOwner(): ScreenLifecycleOwner = ScreenLifecycleHolder.get(key)
}
