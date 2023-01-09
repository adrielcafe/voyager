package cafe.adriel.voyager.dynamic.feature

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import cafe.adriel.voyager.core.registry.ScreenRegistry
import cafe.adriel.voyager.dynamic.feature.custom.dynamicScreen
import cafe.adriel.voyager.dynamic.feature.module.HomeDynamicFeatureScreenProvider
import cafe.adriel.voyager.dynamic.feature.navigation.DynamicFeatureNavigator

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ScreenRegistry {
            register<HomeDynamicFeatureScreenProvider> { screenProvider ->
                dynamicScreen(screenProvider)
            }
        }

        setContent {
            DynamicFeatureNavigator(
                activity = this@MainActivity,
                screen = InitialScreen,
            )
        }
    }
}
