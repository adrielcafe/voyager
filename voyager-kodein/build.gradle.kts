@file:OptIn(ExperimentalKotlinGradlePluginApi::class)

import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinAndroidTarget

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.kotlin.atomicfu)
    id("voyager-base-module")
}

android {
    namespace = "cafe.adriel.voyager.kodein"
}

kotlin {
    applyDefaultHierarchyTemplate {
        common {
            group("commonJvm") {
                withCompilations {
                    it.target.targetName == "desktop" || it.target is KotlinAndroidTarget
                }
            }
            group("nonAndroid") {
                withJs()
                withNative()
                withWasmJs()
                withCompilations {
                    it.target.targetName == "desktop"
                }
            }
            group("commonWeb") {
                withJs()
                withWasmJs()
            }
        }
    }

    js(IR) {
        browser()
    }
    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        browser()
    }

    androidTarget {
        compilerOptions {
            jvmTarget = JvmTarget.JVM_11
        }
    }
    jvm("desktop") {
        compilerOptions {
            jvmTarget = JvmTarget.JVM_11
        }
    }

    iosArm64()
    iosX64()
    iosSimulatorArm64()

    compilerOptions {
        freeCompilerArgs.add("-Xexpect-actual-classes")
    }

    sourceSets {
        commonMain.dependencies {
            api(projects.voyagerCore)
            api(projects.voyagerScreenmodel)
            api(projects.voyagerNavigator)
            implementation(libs.kodein)
            implementation(libs.androidxKmp.runtime)
            implementation(libs.androidxKmp.runtime.saveable)
        }
    }
}
