plugins {
    id("com.android.library")
    kotlin("android")
}

setupModuleForAndroidxCompose(
    composeCompilerVersion = libs.versions.compose.get(),
    withKotlinExplicitMode = false,
)

android {
    namespace = "cafe.adriel.voyager.dynamic.feature.navigation"
}

dependencies {
    implementation(projects.voyagerNavigator)

    implementation(libs.compose.compiler)
    implementation(libs.compose.runtime)

    implementation(libs.kotlin.reflect)

    implementation(libs.play.core)
}
