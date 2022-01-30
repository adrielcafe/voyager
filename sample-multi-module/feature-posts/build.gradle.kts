plugins {
    kotlin("android")
    id("com.android.library")
}

setupModuleForAndroidxCompose(
    composeCompilerVersion = libs.versions.composeCompiler.get(),
    withKotlinExplicitMode = false,
)

dependencies {
    implementation(projects.voyagerNavigator)

    implementation(projects.sampleMultiModule.navigation)

    implementation(libs.appCompat)
    implementation(libs.compose.activity)
    implementation(libs.compose.material)
}
