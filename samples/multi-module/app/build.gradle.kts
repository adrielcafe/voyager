plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("samples-module")
}

android {
    namespace = "cafe.adriel.voyager.sample.multimodule"
    defaultConfig {
        applicationId = "cafe.adriel.voyager.sample.multimodule"
    }
}

dependencies {
    implementation(projects.voyagerNavigator)

    implementation(projects.samples.multiModule.featureHome)
    implementation(projects.samples.multiModule.featurePosts)

    implementation(libs.androidx.activity.compose)
    implementation(samplesCatalog.compose.runtime)
    implementation(samplesCatalog.compose.material)

    debugImplementation(samplesCatalog.leakCanary)
}
