package cafe.adriel.voyager.sample.multimodule.shared

import cafe.adriel.voyager.core.screen.Screen

interface SharedFeatureBWithoutParamScreen : Screen

interface SharedFeatureBWithParamScreen : Screen {

    val params: Params

    data class Params(
        val message: String
    )
}
