plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("org.jetbrains.compose")
    id("com.vanniktech.maven.publish")
}

setupModuleForComposeMultiplatform(fullyMultiplatform = true)


android {
    namespace = "cafe.adriel.voyager.koin"
    defaultConfig {
        consumerProguardFiles("consumer-rules.pro")
    }
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                api(projects.voyagerCore)

                compileOnly(compose.runtime)
                compileOnly(libs.composeMultiplatform.runtimeSaveable)

                implementation(libs.coroutines)
                implementation(libs.koin.compose)
            }
        }
    }
}
