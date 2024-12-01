plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.atomicfu)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.compose.multiplatform)
    id("voyager-kmp-module")
}

android {
    namespace = "cafe.adriel.voyager.core"
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.runtimeSaveable)
            implementation(libs.coroutines.core)
        }
        jvmTest.dependencies {
            implementation(libs.junit.api)
            runtimeOnly(libs.junit.engine)
        }
        androidMain.dependencies {
            implementation(libs.androidx.activity.compose)

            implementation(libs.androidx.lifecycle.runtime)
            implementation(libs.androidx.lifecycle.savedState)
            implementation(libs.androidx.lifecycle.viewModelKtx)
            implementation(libs.androidx.lifecycle.viewModelCompose)
        }
    }
}
