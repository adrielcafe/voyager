plugins {
    kotlin("android")
    kotlin("kapt")
    id("com.android.application")
    id("kotlin-parcelize")
    id("dagger.hilt.android.plugin")
}

setupModuleForAndroidxCompose(
    withKotlinExplicitMode = false,
)

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
    implementation(projects.voyagerNavigator)
    implementation(projects.voyagerTabNavigator)
    implementation(projects.voyagerBottomSheetNavigator)
    implementation(projects.voyagerTransitions)
    implementation(projects.voyagerAndroidx)
    implementation(projects.voyagerHilt)
    implementation(projects.voyagerKodein)
    implementation(projects.voyagerKoin)
    implementation(projects.voyagerRxjava)
    implementation(projects.voyagerLivedata)

    implementation(libs.kodein)
    implementation(libs.koin)
    implementation(libs.appCompat)
    implementation(libs.lifecycle.viewModelKtx)
    implementation(libs.lifecycle.viewModelCompose)
    implementation(libs.compose.rxjava)
    implementation(libs.compose.compiler)
    implementation(libs.compose.runtime)
    implementation(libs.compose.runtimeLiveData)
    implementation(libs.compose.activity)
    implementation(libs.compose.material)
    implementation(libs.compose.materialIcons)
    implementation(libs.compose.animation)
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)

    debugImplementation(libs.leakCanary)
}
