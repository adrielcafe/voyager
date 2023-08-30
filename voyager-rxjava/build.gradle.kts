plugins {
    kotlin("android")
    id("com.android.library")
    id("com.vanniktech.maven.publish")
}

setupModuleForAndroidxCompose()

android {
    namespace = "cafe.adriel.voyager.rxjava"
}

dependencies {
    api(projects.voyagerCore)
    compileOnly(libs.compose.rxjava)

    testImplementation(libs.junit.api)
    testRuntimeOnly(libs.junit.engine)
}
