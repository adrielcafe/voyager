plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
    id("com.vanniktech.maven.publish")
}

setupModuleForAndroidxCompose()

android {
    namespace = "cafe.adriel.voyager.hilt"
}

kapt {
    correctErrorTypes = true
}

dependencies {
    api(projects.voyagerScreenmodel)
    api(projects.voyagerNavigator)

    implementation(libs.compose.runtime)
    implementation(libs.compose.ui)
    implementation(libs.lifecycle.savedState)
    implementation(libs.lifecycle.viewModelKtx)
    implementation(libs.hilt.android)
    implementation(libs.lifecycle.viewModelCompose)
    kapt(libs.hilt.compiler)

    testRuntimeOnly(libs.junit.engine)
    testImplementation(libs.junit.api)
}
