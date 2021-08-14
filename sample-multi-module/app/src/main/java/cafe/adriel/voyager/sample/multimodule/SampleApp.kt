package cafe.adriel.voyager.sample.multimodule

import android.app.Application
import cafe.adriel.voyager.sample.multimodule.featureb.featureBModule
import org.kodein.di.DI
import org.kodein.di.DIAware

class SampleApp : Application(), DIAware {

    override val di by DI.lazy {
        importAll(featureBModule)
    }
}
