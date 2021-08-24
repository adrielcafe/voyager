package cafe.adriel.voyager.sample.multimodule.posts

import cafe.adriel.voyager.core.registry.screenModule
import cafe.adriel.voyager.sample.multimodule.navigation.SharedScreen

val featurePostsScreenModule = screenModule {
    register<SharedScreen.PostList> {
        ListScreen()
    }
    register<SharedScreen.PostDetails> { provider ->
        DetailsScreen(id = provider.id)
    }
}
