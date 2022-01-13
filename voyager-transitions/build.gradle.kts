plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("org.jetbrains.compose")
    id("com.vanniktech.maven.publish")
}

setupModuleForComposeMultiplatform()

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                api(projects.voyagerCore)
                api(projects.voyagerNavigator)
                compileOnly(compose.animation)
            }
        }

        val jvmTest by getting {
            dependencies {
                implementation(libs.junit.api)
                runtimeOnly(libs.junit.engine)
            }
        }

        val desktopTest by getting {
            dependsOn(jvmTest)
        }
        val androidTest by getting {
            dependencies {
                implementation(compose.runtime)
                implementation(compose.ui)
            }
        }
    }
}
