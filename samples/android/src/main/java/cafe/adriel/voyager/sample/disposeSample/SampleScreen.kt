package cafe.adriel.voyager.sample.disposeSample

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SampleScreenModel : ViewModel() {

    init {
        Log.d("SampleScreenModel", "start doing job")
        viewModelScope.launch {
            while (true) {
                Log.d("SampleScreenModel", "Doing job")
                delay(1000)
            }
        }
    }

    override fun onCleared() {
        Log.d("SampleScreenModel", "Disposed")
    }
}

data class SampleScreen(
    private val index: Int
) : Screen {

    override val key: ScreenKey = "SampleScreen$index"

    @Composable
    override fun Content() {
        if (index == 1) {
            viewModel(key = key) { SampleScreenModel() }
        }

        val navigator = LocalNavigator.currentOrThrow

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(40.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "Screen $index")

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = { navigator.push(SampleScreen(index = index + 1)) }
            ) {
                Text(text = "Next")
            }

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = { navigator.pop() }
            ) {
                Text(text = "Back")
            }
        }
    }
}
