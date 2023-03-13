package cafe.adriel.voyager.sample.androidLegacy

import cafe.adriel.voyager.core.model.ScreenModel
import javax.inject.Inject

class LegacyOneScreenModel @Inject constructor() : ScreenModel {
    val text = "I'm legacy one screen model"

    override fun onDispose() {
        println(">>>> disposing $this")
        super.onDispose()
    }
}
