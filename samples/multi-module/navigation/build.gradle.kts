plugins {
    kotlin("android")
    id("com.android.library")
}

setupModuleForAndroidxCompose(
    composeCompilerVersion = libs.versions.composeCompiler.get(),
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
