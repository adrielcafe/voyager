import org.jetbrains.kotlin.gradle.dsl.ExplicitApiMode

plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("org.jetbrains.compose")
}

kotlin {
    explicitApi = ExplicitApiMode.Strict

    android()
    jvm()

    sourceSets {
        val commonMain by getting {
            dependencies {
                api(projects.voyagerCore)
                implementation(compose.runtime)
                implementation(compose.ui)
            }
        }
        val androidMain by getting {
            dependencies {
                implementation(libs.compose.activity)
            }
        }
    }
}

android {
    compileSdk = 31
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdk = 21
        targetSdk = 31
    }
}
