package cafe.adriel.voyager.sample

import android.app.Application
import cafe.adriel.voyager.sample.androidNavigation.DetailsViewModel
import cafe.adriel.voyager.sample.androidNavigation.ListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            modules(
                module {
                    viewModel {
                        ListViewModel(handle = get())
                    }
                    viewModel { parameters ->
                        DetailsViewModel(index = parameters.get())
                    }
                }
            )
        }
    }
}
