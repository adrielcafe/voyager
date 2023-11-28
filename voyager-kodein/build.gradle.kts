plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("org.jetbrains.compose")
    id("com.vanniktech.maven.publish")
}

setupModuleForComposeMultiplatform(fullyMultiplatform = true)

android {
    namespace = "cafe.adriel.voyager.kodein"
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                api(projects.voyagerCore)
                api(projects.voyagerScreenmodel)
                api(projects.voyagerNavigator)
                compileOnly(compose.runtime)
                compileOnly(compose.runtimeSaveable)
                implementation(libs.kodein)
            }
        }

        val jvmTest by getting {
            dependencies {
                implementation(libs.junit.api)
                runtimeOnly(libs.junit.engine)
            }
        }
    }
}
