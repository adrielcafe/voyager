package cafe.adriel.voyager.sample.multimodule.featureb

import cafe.adriel.voyager.sample.multimodule.shared.SharedFeatureBParamScreen
import cafe.adriel.voyager.sample.multimodule.shared.SharedFeatureBScreen
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.factory
import org.kodein.di.provider

val featureBModule = DI.Module(name = "feature-b") {

    bind<SharedFeatureBScreen> {
        provider { FeatureBScreen() }
    }

    bind<SharedFeatureBParamScreen> {
        factory { params: SharedFeatureBParamScreen.Params -> FeatureBParamScreen(params) }
    }
}
