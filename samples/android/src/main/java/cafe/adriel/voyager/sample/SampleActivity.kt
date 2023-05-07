package cafe.adriel.voyager.sample

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.sample.androidLegacy.LegacyActivity
import cafe.adriel.voyager.sample.androidViewModel.AndroidViewModelActivity
import cafe.adriel.voyager.sample.basicNavigation.BasicNavigationActivity
import cafe.adriel.voyager.sample.bottomSheetNavigation.BottomSheetNavigationActivity
import cafe.adriel.voyager.sample.hiltIntegration.HiltMainActivity
import cafe.adriel.voyager.sample.kodeinIntegration.KodeinIntegrationActivity
import cafe.adriel.voyager.sample.koinIntegration.KoinIntegrationActivity
import cafe.adriel.voyager.sample.liveDataIntegration.LiveDataIntegrationActivity
import cafe.adriel.voyager.sample.nestedNavigation.NestedNavigationActivity
import cafe.adriel.voyager.sample.parcelableScreen.ParcelableActivity
import cafe.adriel.voyager.sample.rxJavaIntegration.RxJavaIntegrationActivity
import cafe.adriel.voyager.sample.screenModel.ScreenModelActivity
import cafe.adriel.voyager.sample.stateStack.StateStackActivity
import cafe.adriel.voyager.sample.tabNavigation.TabNavigationActivity

class SampleActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            Content()
        }
    }

    @Composable
    fun Content() {
        LazyColumn(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(24.dp)
        ) {
            item {
                StartSampleButton<StateStackActivity>("SnapshotStateStack")
                StartSampleButton<BasicNavigationActivity>("Basic Navigation")
                StartSampleButton<ParcelableActivity>("Basic Navigation with Parcelable")
                StartSampleButton<TabNavigationActivity>("Tab Navigation")
                StartSampleButton<BottomSheetNavigationActivity>("BottomSheet Navigation")
                StartSampleButton<NestedNavigationActivity>("Nested Navigation")
                StartSampleButton<AndroidViewModelActivity>("Android ViewModel")
                StartSampleButton<ScreenModelActivity>("ScreenModel")
                StartSampleButton<KoinIntegrationActivity>("Koin Integration")
                StartSampleButton<KodeinIntegrationActivity>("Kodein Integration")
                StartSampleButton<RxJavaIntegrationActivity>("RxJava Integration")
                StartSampleButton<LiveDataIntegrationActivity>("LiveData Integration")
                StartSampleButton<HiltMainActivity>("Hilt Integration")
                StartSampleButton<LegacyActivity>("Legacy Integration")
            }
        }
    }

    @Composable
    private inline fun <reified T : Activity> StartSampleButton(text: String) {
        val context = LocalContext.current

        Button(
            onClick = { context.startActivity(Intent(this, T::class.java)) },
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
        ) {
            Text(text = text)
        }
    }
}
