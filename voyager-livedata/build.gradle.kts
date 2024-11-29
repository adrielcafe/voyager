plugins {
    id("com.android.library")
    kotlin("android")
    id("com.vanniktech.maven.publish")
}

setupModuleForAndroidxCompose()

android {
    namespace = "cafe.adriel.voyager.livedata"
}

dependencies {
    api(projects.voyagerCore)
    api(projects.voyagerScreenmodel)

    implementation(libs.compose.runtimeLiveData)

    testRuntimeOnly(libs.junit.engine)
    testImplementation(libs.junit.api)
}
