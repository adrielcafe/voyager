plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("com.vanniktech.maven.publish")
}

kotlin {
    explicitApi()

    android {
        publishAllLibraryVariants()
    }
    jvm("desktop")

    sourceSets {
        /* Source sets structure
        common
          ├─ jvm
              ├─ android
              ├─ desktop
         */
        val commonMain by getting
        val jvmMain by creating {
            dependsOn(commonMain)
            dependencies {
                api(projects.voyagerCore)
                compileOnly(libs.compose.rxjava)
            }
        }
        val desktopMain by getting {
            dependsOn(jvmMain)
        }
        val androidMain by getting {
            dependsOn(jvmMain)
        }
        val commonTest by getting
        val jvmTest by creating {
            dependsOn(commonTest)
            dependencies {
                implementation(libs.junit.api)
                runtimeOnly(libs.junit.engine)
            }
        }
        val desktopTest by getting {
            dependsOn(jvmTest)
        }
        val androidTest by getting {
            dependsOn(jvmTest)
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
