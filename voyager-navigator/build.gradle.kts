plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.compose.multiplatform)
    id("voyager-kmp-module")
}

android {
    namespace = "cafe.adriel.voyager.navigator"
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(projects.voyagerCore)
            implementation(compose.runtime)
            implementation(compose.runtimeSaveable)
        }
        androidMain.dependencies {
            implementation(libs.androidx.activity.compose)
        }
    }
}
