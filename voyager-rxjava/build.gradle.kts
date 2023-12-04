plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("com.vanniktech.maven.publish")
}

setupModuleForComposeMultiplatform()

android {
    namespace = "cafe.adriel.voyager.rxjava"
}

kotlin {
    sourceSets {
        val jvmMain by getting {
            dependencies {
                api(projects.voyagerCore)
                compileOnly(libs.rxjava)
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
