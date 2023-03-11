import org.jetbrains.compose.desktop.application.dsl.TargetFormat.Deb
import org.jetbrains.compose.desktop.application.dsl.TargetFormat.Dmg
import org.jetbrains.compose.desktop.application.dsl.TargetFormat.Msi
import org.jetbrains.compose.experimental.dsl.IOSDevices
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    kotlin("multiplatform")
    id("com.android.application")
    id("org.jetbrains.compose")
}

setupModuleForComposeMultiplatform(
    fullyMultiplatform = true,
    withKotlinExplicitMode = false
)

android {
    namespace = "cafe.adriel.voyager.sample.multiplatform"
}

kotlin {
    macosX64 {
        binaries {
            executable {
                entryPoint = "main"
                freeCompilerArgs += listOf(
                    "-linker-option", "-framework", "-linker-option", "Metal"
                )
            }
        }
    }
    macosArm64 {
        binaries {
            executable {
                entryPoint = "main"
                freeCompilerArgs += listOf(
                    "-linker-option", "-framework", "-linker-option", "Metal"
                )
            }
        }
    }
    val uikitConfiguration: KotlinNativeTarget.() -> Unit = {
        binaries {
            executable() {
                entryPoint = "main"
                freeCompilerArgs += listOf(
                    "-linker-option", "-framework", "-linker-option", "Metal",
                    "-linker-option", "-framework", "-linker-option", "CoreText",
                    "-linker-option", "-framework", "-linker-option", "CoreGraphics"
                )
            }
        }
    }
    iosX64("uikitX64", uikitConfiguration)
    iosArm64("uikitArm64", uikitConfiguration)
    iosSimulatorArm64("uikitSimulatorArm64", uikitConfiguration)

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(compose.material)
                implementation(compose.runtime)

                implementation(projects.voyagerCore)
                implementation(projects.voyagerNavigator)
                implementation(libs.coroutines)
            }
        }

        val androidMain by getting {
            dependencies {
                implementation(libs.appCompat)
                implementation(libs.compose.activity)
            }
        }

        val desktopMain by getting {
            dependencies {
                implementation(compose.desktop.currentOs)
            }
        }
    }
}

android {
    defaultConfig {
        applicationId = "cafe.adriel.voyager.sample.multiplatform"
    }
}

compose.desktop {
    application {
        mainClass = "cafe.adriel.voyager.sample.multiplatform.AppKt"
        nativeDistributions {
            targetFormats(Dmg, Msi, Deb)
            packageName = "jvm"
            packageVersion = "1.0.0"
        }
    }
}

compose.desktop.nativeApplication {
    targets(kotlin.targets.getByName("macosX64"))
    distributions {
        targetFormats(Dmg)
        packageName = "MultiplatformSample"
        packageVersion = "1.0.0"
    }
}

compose.experimental {
    uikit.application {
        bundleIdPrefix = "cafe.adriel.voyager"
        projectName = "MultiplatformSample"
        deployConfigurations {
            simulator("IPhone8") {
                //Usage: ./gradlew iosDeployIPhone8Debug
                device = IOSDevices.IPHONE_8
            }
            simulator("IPad") {
                //Usage: ./gradlew iosDeployIPadDebug
                device = IOSDevices.IPAD_MINI_6th_Gen
            }
        }
    }
}

kotlin {
    targets.withType<KotlinNativeTarget> {
        binaries.all {
            // TODO: the current compose binary surprises LLVM, so disable checks for now.
            freeCompilerArgs += "-Xdisable-phases=VerifyBitcode"
        }
    }
}
