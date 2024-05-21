plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("org.jetbrains.compose")
    id("com.vanniktech.maven.publish")
}

setupModuleForComposeMultiplatform(fullyMultiplatform = true)

android {
    namespace = "cafe.adriel.voyager.navigator"
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(projects.voyagerCore)
            implementation(libs.coroutines.core)
            compileOnly(compose.runtime)
            compileOnly(compose.runtimeSaveable)
        }

        jvmTest.dependencies {
            implementation(libs.junit.api)
            runtimeOnly(libs.junit.engine)
        }

        androidMain.dependencies {
            implementation(libs.compose.activity)
        }
    }
}
