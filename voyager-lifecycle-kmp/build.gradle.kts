plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.compose.multiplatform)
    id("voyager-kmp-module")
}

android {
    namespace = "cafe.adriel.voyager.lifecycle.kmp"
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
            runtimeOnly(libs.junit.engine)
            implementation(libs.junit.api)
        }
        androidMain.dependencies {
            compileOnly(libs.androidx.lifecycle.savedState)
            compileOnly(compose.ui)
        }
    }
}
