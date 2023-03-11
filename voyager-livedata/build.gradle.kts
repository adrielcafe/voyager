plugins {
    kotlin("android")
    id("com.android.library")
    id("com.vanniktech.maven.publish")
}

setupModuleForAndroidxCompose(
    composeCompilerVersion = libs.versions.composeCompiler.get()
)

android {
    namespace = "cafe.adriel.voyager.livedata"
    defaultConfig {
        consumerProguardFiles("consumer-rules.pro")
    }
}

dependencies {
    api(projects.voyagerCore)

    implementation(libs.compose.runtimeLiveData)

    testRuntimeOnly(libs.junit.engine)
    testImplementation(libs.junit.api)
}
