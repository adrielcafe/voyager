package cafe.adriel.voyager.sample.tabNavigation.tabs

import androidx.compose.ui.graphics.painter.Painter
import cafe.adriel.voyager.navigator.tab.TabOptions

data class TabOptionsModel(
    override val index: UShort,
    override val title: String,
    override val icon: Painter?
): TabOptions
