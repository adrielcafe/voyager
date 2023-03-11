package cafe.adriel.voyager.sample.hiltIntegration

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.sample.sampleItems
import javax.inject.Inject

class HiltListScreenModel @Inject constructor() : ScreenModel {

    val items = sampleItems
}
