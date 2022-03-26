package cafe.adriel.voyager.sample.multimodule.navigation

import cafe.adriel.voyager.core.registry.ScreenProvider

sealed class SharedScreen : ScreenProvider {
    object PostList : SharedScreen()
    data class PostDetails(val id: String) : SharedScreen()
}
