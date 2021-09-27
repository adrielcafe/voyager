package cafe.adriel.voyager.sample.hiltIntegration

import androidx.lifecycle.ViewModel

// Until now, there is no support to assisted injection.
// Follow the thread here: https://github.com/google/dagger/issues/2287
class HiltDetailsViewModel(
    val index: Int
) : ViewModel()
