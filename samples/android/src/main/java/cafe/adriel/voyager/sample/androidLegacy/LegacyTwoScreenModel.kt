package cafe.adriel.voyager.sample.androidLegacy

import cafe.adriel.voyager.core.model.ScreenModel
import javax.inject.Inject

class LegacyTwoScreenModel @Inject constructor() : ScreenModel {
    val text = "I'm legacy two screen model"

    override fun onDispose() {
        println(">>>> disposing $this")
        super.onDispose()
    }
}
