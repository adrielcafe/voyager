plugins {
    kotlin("android")
    id("com.android.library")
}

setupModuleForAndroidxCompose(
    composeCompilerVersion = libs.versions.compose.get(),
    withKotlinExplicitMode = false,
)

dependencies {
    implementation(projects.voyagerCore)

    implementation(libs.compose.compiler)
    implementation(libs.compose.runtime)
}
