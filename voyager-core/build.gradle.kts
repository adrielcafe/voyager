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
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                compileOnly(compose.runtime)
                compileOnly(compose.runtimeSaveable)
                implementation(libs.coroutines.core)
            }
        }
        val jvmTest by getting {
            dependencies {
                implementation(libs.junit.api)
                runtimeOnly(libs.junit.engine)
            }
        }
        val androidMain by getting {
            dependencies {
                implementation(libs.compose.activity)

                implementation(libs.lifecycle.runtime)
                implementation(libs.lifecycle.savedState)
                implementation(libs.lifecycle.viewModelKtx)
                implementation(libs.lifecycle.viewModelCompose)
            }
        }
        val commonWebMain by getting {
            dependencies {
                implementation(libs.multiplatformUuid)
            }
        }
    }
}
