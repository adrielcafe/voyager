plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("org.jetbrains.compose")
    id("com.vanniktech.maven.publish")
}

setupModuleForComposeMultiplatform(
    fullyMultiplatform = true
)

android {
    namespace = "cafe.adriel.voyager.koin"
    defaultConfig {
        consumerProguardFiles("consumer-rules.pro")
    }
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(projects.voyagerCore)
            api(projects.voyagerScreenmodel)
            api(projects.voyagerNavigator)

            compileOnly(compose.runtime)
            compileOnly(compose.runtimeSaveable)

            implementation(libs.coroutines.core)
        }
    }
}

dependencies {
    commonMainImplementation(libs.koin.compose) {
        exclude("org.jetbrains.compose.runtime")
    }
}
