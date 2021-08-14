package cafe.adriel.voyager.sample.multimodule.featureb

import cafe.adriel.voyager.sample.multimodule.shared.SharedFeatureBWithParamScreen
import cafe.adriel.voyager.sample.multimodule.shared.SharedFeatureBWithoutParamScreen
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.factory
import org.kodein.di.provider

val featureBModule = DI.Module(name = "feature-b") {

    bind<SharedFeatureBWithoutParamScreen> {
        provider { FeatureBWithoutParamScreen() }
    }

    bind<SharedFeatureBWithParamScreen> {
        factory { params: SharedFeatureBWithParamScreen.Params -> FeatureBWithParamScreen(params) }
    }
}
