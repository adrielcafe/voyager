package cafe.adriel.voyager.sample.kodeinIntegration

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import cafe.adriel.voyager.core.screen.uniqueScreenKey
import cafe.adriel.voyager.kodein.ScreenContext
import cafe.adriel.voyager.kodein.rememberScreenModel
import cafe.adriel.voyager.sample.ListContent
import org.kodein.di.compose.localDI
import org.kodein.di.instance
import org.kodein.di.on

class KodeinScreen : Screen {

    override val key: ScreenKey = uniqueScreenKey

    @Composable
    override fun Content() {
        val screenModel = rememberScreenModel<KodeinScreenModel>()
        val scopedDependency by localDI().on(ScreenContext(this)).instance<KodeinScopedDependencySample>()

        Column {
            Text(scopedDependency.toString())
            ListContent(screenModel.items)
        }
    }
}
