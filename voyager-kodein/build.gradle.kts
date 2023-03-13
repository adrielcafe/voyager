plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("org.jetbrains.compose")
    id("com.vanniktech.maven.publish")
}

// Support fully when https://github.com/kosi-libs/Kodein/pull/431 get merged.
setupModuleForComposeMultiplatform()

android {
    namespace = "cafe.adriel.voyager.kodein"
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                api(projects.voyagerCore)
                implementation(compose.runtime)
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
