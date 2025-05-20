import extensions.capitalize
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.compose.desktop.application.tasks.AbstractNativeMacApplicationPackageTask
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.compose.multiplatform)
    id("samples-module")
}

android {
    namespace = "cafe.adriel.voyager.sample.multiplatform"
    defaultConfig {
        applicationId = "cafe.adriel.voyager.sample.multiplatform"
    }
}

kotlin {
    applyDefaultHierarchyTemplate()

    listOf(
        macosX64(),
        macosArm64()
    ).forEach { macosTarget ->
        macosTarget.binaries {
            executable {
                entryPoint = "main"
                freeCompilerArgs += listOf(
                    "-linker-option",
                    "-framework",
                    "-linker-option",
                    "Metal"
                )
            }
        }
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
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

    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        outputModuleName = "composeApp"
        browser {
            commonWebpackConfig {
                outputFileName = "composeApp.js"
            }
        }
        binaries.executable()
    }

    js(IR) {
        browser()
        binaries.executable()
    }

    sourceSets {
        commonMain.dependencies {
            implementation(projects.voyagerCore)
            implementation(projects.voyagerNavigator)
            implementation(projects.voyagerTransitions)
            implementation(compose.runtime)
            implementation(compose.ui)
            implementation(compose.material)
            implementation(libs.coroutines.core)
        }
        androidMain.dependencies {
            implementation(libs.androidx.activity.compose)
        }
        getByName("desktopMain").dependencies {
            implementation(compose.desktop.currentOs)
        }
    }
}

compose {
    desktop {
        application {
            mainClass = "cafe.adriel.voyager.sample.multiplatform.AppKt"
            nativeDistributions {
                targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
                packageName = "jvm"
                packageVersion = "1.0.0"
            }
        }
        nativeApplication {
            targets(kotlin.targets.getByName("macosX64"))
            distributions {
                targetFormats(TargetFormat.Dmg)
                packageName = "MultiplatformSample"
                packageVersion = "1.0.0"
            }
        }
    }
}

afterEvaluate {
    val baseTask = "createDistributableNative"
    listOf("debug", "release").forEach { buildType ->
        val createAppTaskName = baseTask + buildType.capitalize() + "macosX64".capitalize()

        val createAppTask = tasks.findByName(createAppTaskName) as? AbstractNativeMacApplicationPackageTask?
            ?: return@forEach

        val destinationDir = createAppTask.destinationDir.get().asFile
        val packageName = createAppTask.packageName.get()

        tasks.register<Task>(name = "runNative${buildType.capitalize()}") {
            group = createAppTask.group
            dependsOn(createAppTaskName)
            doLast {
                ProcessBuilder("open", destinationDir.absolutePath + "/" + packageName + ".app").start().waitFor()
            }
        }
    }
}
