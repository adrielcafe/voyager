package cafe.adriel.voyager.sample.screenTransition

import cafe.adriel.voyager.transitions.ScreenTransition

data class NoCustomAnimationSampleScreen(
    override val index: Int
) : BaseSampleScreen()

data class FadeAnimationSampleScreen(
    override val index: Int
) : BaseSampleScreen(), ScreenTransition by FadeTransition()
