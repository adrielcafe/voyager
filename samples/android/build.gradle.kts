plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.hilt)
    id("samples-module")
}

android {
    namespace = "cafe.adriel.voyager.sample"
    defaultConfig {
        applicationId = "cafe.adriel.voyager.sample"
    }
}

kapt {
    correctErrorTypes = true
}

dependencies {
    implementation(projects.voyagerScreenmodel)
    implementation(projects.voyagerNavigator)
    implementation(projects.voyagerTabNavigator)
    implementation(projects.voyagerBottomSheetNavigator)
    implementation(projects.voyagerTransitions)
    implementation(projects.voyagerHilt)
    implementation(projects.voyagerKodein)
    implementation(projects.voyagerKoin)
    implementation(projects.voyagerRxjava)
    implementation(projects.voyagerLivedata)

    kapt(libs.hilt.compiler)
    implementation(libs.hilt.android)
    implementation(libs.kodein)
    implementation(libs.androidx.lifecycle.viewModelKtx)
    implementation(libs.androidx.lifecycle.viewModelCompose)
    implementation(libs.androidx.activity.compose)
    implementation(samplesCatalog.koin)
    implementation(samplesCatalog.compose.rxjava)
    implementation(samplesCatalog.compose.livedata)
    implementation(samplesCatalog.compose.material)
    implementation(samplesCatalog.compose.materialIcons)

    debugImplementation(samplesCatalog.leakCanary)
}
