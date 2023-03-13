plugins {
    kotlin("android")
    id("com.android.library")
}

setupModuleForAndroidxCompose(
    withKotlinExplicitMode = false,
)

android {
    namespace = "cafe.adriel.voyager.sample.multimodule.posts"
}

dependencies {
    implementation(projects.voyagerNavigator)

    implementation(projects.samples.multiModule.navigation)

    implementation(libs.appCompat)
    implementation(libs.compose.activity)
    implementation(libs.compose.material)
}
