plugins {
    kotlin("android")
    id("com.android.application")
}

setupModuleForAndroidxCompose(
    composeCompilerVersion = libs.versions.compose.get(),
    withKotlinExplicitMode = false,
)

android {
    defaultConfig {
        applicationId = "cafe.adriel.voyager.sample.multimodule"
    }
}

dependencies {
    implementation(projects.voyagerNavigator)

    implementation(projects.sampleMultiModule.featureHome)
    implementation(projects.sampleMultiModule.featurePosts)

    implementation(libs.appCompat)
    implementation(libs.compose.compiler)
    implementation(libs.compose.runtime)
    implementation(libs.compose.activity)
    implementation(libs.compose.material)

    debugImplementation(libs.leakCanary)
}
