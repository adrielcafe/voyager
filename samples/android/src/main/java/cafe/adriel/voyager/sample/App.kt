package cafe.adriel.voyager.sample

import android.app.Application
import cafe.adriel.voyager.sample.androidViewModel.AndroidDetailsViewModel
import cafe.adriel.voyager.sample.androidViewModel.AndroidListViewModel
import cafe.adriel.voyager.sample.kodeinIntegration.KodeinScreenModel
import cafe.adriel.voyager.sample.koinIntegration.KoinScreenModel
import dagger.hilt.android.HiltAndroidApp
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.bindProvider
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

@HiltAndroidApp
class App : Application(), DIAware {

    override val di by DI.lazy {
        androidXModule(this@App)
        bindProvider { KodeinScreenModel() }
    }

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(
                module {
                    factoryOf(::KoinScreenModel)

                    viewModel {
                        AndroidListViewModel(handle = get())
                    }
                    viewModel { parameters ->
                        AndroidDetailsViewModel(index = parameters.get())
                    }
                }
            )
        }
    }
}
