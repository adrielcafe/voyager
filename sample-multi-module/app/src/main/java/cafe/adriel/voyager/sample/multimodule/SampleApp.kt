package cafe.adriel.voyager.sample.multimodule

import android.app.Application
import cafe.adriel.voyager.core.registry.ScreenRegistry
import cafe.adriel.voyager.sample.multimodule.posts.featurePostsScreenModule

class SampleApp : Application() {

    override fun onCreate() {
        super.onCreate()

        ScreenRegistry {
            featurePostsScreenModule()
        }
    }
}
