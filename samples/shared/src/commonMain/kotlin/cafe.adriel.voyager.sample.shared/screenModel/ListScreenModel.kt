package cafe.adriel.voyager.sample.shared.screenModel

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.sample.shared.sampleItems

class ListScreenModel : ScreenModel {

    val items = sampleItems

    override fun onDispose() {
        println("ScreenModel: dispose list")
    }
}
