plugins {
    kotlin("android")
    id("com.android.library")
    id("com.vanniktech.maven.publish")
}

setupModuleForAndroidxCompose()

android {
    namespace = "cafe.adriel.voyager.androidx"
    defaultConfig {
        consumerProguardFiles("consumer-rules.pro")
    }
}

dependencies {
    api(projects.voyagerCore)

    implementation(libs.lifecycle.runtime)
    implementation(libs.lifecycle.savedState)
    implementation(libs.lifecycle.viewModelKtx)
    implementation(libs.lifecycle.viewModelCompose)
    implementation(libs.compose.runtimeSaveable)

    testRuntimeOnly(libs.junit.engine)
    testImplementation(libs.junit.api)
}
