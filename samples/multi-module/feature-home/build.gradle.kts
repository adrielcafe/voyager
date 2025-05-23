plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("samples-module")
}

android {
    namespace = "cafe.adriel.voyager.sample.multimodule.home"
}

dependencies {
    implementation(projects.voyagerNavigator)

    implementation(projects.samples.multiModule.navigation)

    implementation(libs.androidx.activity.compose)
    implementation(samplesCatalog.compose.material)
}
