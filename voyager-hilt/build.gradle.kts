plugins {
    kotlin("android")
    kotlin("kapt")
    id("com.android.library")
    id("com.vanniktech.maven.publish")
}

setupModuleForAndroidxCompose(
    composeCompilerVersion = libs.versions.compose.get()
)

android {
    defaultConfig {
        consumerProguardFiles("consumer-rules.pro")
    }
}

kapt {
    correctErrorTypes = true
}

dependencies {
    api(projects.voyagerAndroidx)

    implementation(libs.compose.runtime)
    implementation(libs.compose.ui)
    implementation(libs.lifecycle.savedState)
    implementation(libs.lifecycle.viewModelKtx)
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)

    testRuntimeOnly(libs.junit.engine)
    testImplementation(libs.junit.api)
}
