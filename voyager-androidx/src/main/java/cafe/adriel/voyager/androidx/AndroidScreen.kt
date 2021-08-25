package cafe.adriel.voyager.androidx

import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.uniqueScreenKey

public abstract class AndroidScreen : Screen, ScreenLifecycleOwner by ScreenLifecycleHolder() {

    override val key: String = uniqueScreenKey
}
