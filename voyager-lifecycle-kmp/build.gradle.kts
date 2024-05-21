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
        commonMain.dependencies {
            api(projects.voyagerCore)
            api(projects.voyagerNavigator)
            compileOnly(compose.runtime)
            compileOnly(compose.runtimeSaveable)
            implementation(libs.androidxKmp.lifecycle.viewmodelCompose)
            implementation(libs.androidxKmp.lifecycle.viewmodel)
            implementation(libs.androidxKmp.lifecycle.runtimeCompose)
            implementation(libs.androidxKmp.core.bundle)
        }

        jvmTest.dependencies {
            implementation(libs.junit.api)
            runtimeOnly(libs.junit.engine)
        }

        androidMain.dependencies {
            compileOnly(libs.lifecycle.savedState)
            compileOnly(compose.ui)
        }
    }
}
