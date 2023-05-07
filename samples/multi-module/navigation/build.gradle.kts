plugins {
    kotlin("android")
    id("com.android.library")
}

setupModuleForAndroidxCompose(
    withKotlinExplicitMode = false,
)

android {
    namespace = "cafe.adriel.voyager.sample.multimodule.navigation"
}

dependencies {
    implementation(projects.voyagerCore)

    implementation(libs.compose.compiler)
    implementation(libs.compose.runtime)
}
