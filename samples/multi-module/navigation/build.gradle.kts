plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("samples-module")
}

android {
    namespace = "cafe.adriel.voyager.sample.multimodule.navigation"
}

dependencies {
    implementation(projects.voyagerCore)
    implementation(samples.compose.runtime)
}
