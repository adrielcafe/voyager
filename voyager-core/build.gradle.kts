plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("org.jetbrains.compose")
    id("com.vanniktech.maven.publish")
    id("kotlinx-atomicfu")
}

setupModuleForComposeMultiplatform(fullyMultiplatform = true)

android {
    namespace = "cafe.adriel.voyager.core"
    defaultConfig {
        consumerProguardFiles("consumer-rules.pro")
    }
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            compileOnly(compose.runtime)
            compileOnly(compose.runtimeSaveable)
            implementation(libs.coroutines.core)
            api(libs.jetbrains.lifecycle.runtime.compose)
            api(libs.jetbrains.lifecycle.savedState)
            api(libs.jetbrains.core.bundle)
        }
        jvmTest.dependencies {
            implementation(libs.junit.api)
            runtimeOnly(libs.junit.engine)
        }
        androidMain.dependencies {
            implementation(libs.compose.activity)

            implementation(libs.lifecycle.viewModelKtx)
            implementation(libs.lifecycle.viewModelCompose)
        }
        val commonWebMain by getting {
            dependencies {
                implementation(libs.multiplatformUuid)
            }
        }
    }
}
