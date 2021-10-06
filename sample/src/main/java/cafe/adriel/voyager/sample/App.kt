package cafe.adriel.voyager.sample

import android.app.Application
import cafe.adriel.voyager.sample.androidViewModel.AndroidDetailsViewModel
import cafe.adriel.voyager.sample.androidViewModel.AndroidListViewModel
import cafe.adriel.voyager.sample.kodeinIntegration.KodeinScreenModel
import cafe.adriel.voyager.sample.koinIntegration.KoinScreenModel
import dagger.hilt.android.HiltAndroidApp
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.bindProvider
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.factory
import org.koin.dsl.module

@HiltAndroidApp
class App : Application(), DIAware {

    override val di by DI.lazy {
        bindProvider { KodeinScreenModel() }
    }

    override fun onCreate() {
        super.onCreate()
        startKoin {
            modules(
                module {
                    factory<KoinScreenModel>()

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
