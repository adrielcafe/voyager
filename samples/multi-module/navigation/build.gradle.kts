plugins {
    id("com.android.library")
    kotlin("android")
}

setupModuleForAndroidxCompose(
    withKotlinExplicitMode = false
)

android {
    namespace = "cafe.adriel.voyager.sample.multimodule.navigation"
}

dependencies {
    implementation(projects.voyagerCore)

    implementation(libs.compose.compiler)
    implementation(libs.compose.runtime)
}
