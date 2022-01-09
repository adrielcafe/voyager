plugins {
    kotlin("android")
    id("com.android.library")
    id("com.vanniktech.maven.publish")
}

setupModuleForAndroidxCompose(
    composeCompilerVersion = libs.versions.composeCompiler.get()
)

android {
    defaultConfig {
        consumerProguardFiles("consumer-rules.pro")
    }
}

dependencies {
    api(projects.voyagerCore)

    implementation(libs.koin)
    implementation(libs.compose.runtime)

    testRuntimeOnly(libs.junit.engine)
    testImplementation(libs.junit.api)
}
