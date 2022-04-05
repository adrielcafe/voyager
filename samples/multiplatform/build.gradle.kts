import org.jetbrains.compose.compose
import org.jetbrains.compose.desktop.application.dsl.TargetFormat.Deb
import org.jetbrains.compose.desktop.application.dsl.TargetFormat.Dmg
import org.jetbrains.compose.desktop.application.dsl.TargetFormat.Msi

plugins {
    kotlin("multiplatform")
    id("com.android.application")
    id("org.jetbrains.compose")
}

setupModuleForComposeMultiplatform()

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(compose.material)
                implementation(compose.runtime)

                implementation(projects.voyagerCore)
                implementation(projects.voyagerNavigator)
                implementation(libs.coroutines.core)
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
