plugins {
    id("com.android.dynamic-feature")
    kotlin("android")
}

setupModuleForAndroidxCompose(
    composeCompilerVersion = libs.versions.compose.get(),
    withKotlinExplicitMode = false,
)

android {
    namespace = "cafe.adriel.voyager.dynamic.feature.details"
}

dependencies {
    implementation(projects.samples.dynamicFeature.app)
    implementation(projects.voyagerNavigator)

    implementation(libs.appCompat)
    implementation(libs.compose.activity)
    implementation(libs.compose.material)
    implementation(libs.compose.ui)
}
