import extensions.kotlinMultiplatform
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinAndroidTarget

@OptIn(ExperimentalKotlinGradlePluginApi::class)
kotlinMultiplatform {
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
            jvmTarget = JvmTarget.JVM_1_8
        }
    }
    jvm("desktop") {
        compilerOptions {
            jvmTarget = JvmTarget.JVM_1_8
        }
    }

    macosX64()
    macosArm64()

    iosArm64()
    iosX64()
    iosSimulatorArm64()

    compilerOptions {
        freeCompilerArgs.add("-Xexpect-actual-classes")
    }
}
