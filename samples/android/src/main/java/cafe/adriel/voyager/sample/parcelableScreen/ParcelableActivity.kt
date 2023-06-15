package cafe.adriel.voyager.sample.parcelableScreen

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import cafe.adriel.voyager.navigator.Navigator

class ParcelableActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            Navigator(
                screen = SampleParcelableScreen(parcelable = ParcelableContent(index = 0)),
                onBackPressed = { currentScreen ->
                    Log.d("Navigator", "Pop screen #${(currentScreen as SampleParcelableScreen).parcelable.index}")
                    true
                }
            )
        }
    }
}
