plugins {
    id("com.android.application")
    kotlin("android")
}

setupModuleForAndroidxCompose(
    composeCompilerVersion = libs.versions.compose.get(),
    withKotlinExplicitMode = false,
)

android {
    namespace = "cafe.adriel.voyager.dynamic.feature"
    defaultConfig {
        applicationId = "cafe.adriel.voyager.dynamic.feature"
    }
    dynamicFeatures += listOf(
        ":samples:dynamic-feature:home",
        ":samples:dynamic-feature:details"
    )
}

dependencies {
    api(projects.samples.dynamicFeature.navigation)
    implementation(projects.voyagerNavigator)

    implementation(libs.appCompat)
    implementation(libs.compose.activity)
    implementation(libs.compose.material)
    implementation(libs.compose.ui)

    implementation(libs.play.core)

    implementation("com.google.android.material:material:1.5.0")
}
