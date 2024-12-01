plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.kapt)
    id("voyager-android-module")
}

android {
    namespace = "cafe.adriel.voyager.hilt"
}

kapt {
    correctErrorTypes = true
}

dependencies {
    api(projects.voyagerScreenmodel)
    api(projects.voyagerNavigator)

    implementation(libs.androidx.lifecycle.savedState)
    implementation(libs.androidx.lifecycle.viewModelKtx)
    implementation(libs.androidx.lifecycle.viewModelCompose)
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)

    testRuntimeOnly(libs.junit.engine)
    testImplementation(libs.junit.api)
}
