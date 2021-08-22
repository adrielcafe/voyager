package cafe.adriel.voyager.sample

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.sample.androidNavigation.AndroidNavigationActivity
import cafe.adriel.voyager.sample.basicNavigation.BasicNavigationActivity
import cafe.adriel.voyager.sample.nestedNavigation.NestedNavigationActivity
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
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
        ) {
            StartSampleButton<StateStackActivity>("SnapshotStateStack")
            Spacer(modifier = Modifier.height(24.dp))
            StartSampleButton<BasicNavigationActivity>("Basic Navigation")
            Spacer(modifier = Modifier.height(24.dp))
            StartSampleButton<TabNavigationActivity>("Tab Navigation")
            Spacer(modifier = Modifier.height(24.dp))
            StartSampleButton<NestedNavigationActivity>("Nested Navigation")
            Spacer(modifier = Modifier.height(24.dp))
            StartSampleButton<AndroidNavigationActivity>("Android Navigation")
        }
    }

    @Composable
    private inline fun <reified T : Activity> StartSampleButton(text: String) {
        val context = LocalContext.current

        Button(
            onClick = { context.startActivity(Intent(this, T::class.java)) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = text)
        }
    }
}
