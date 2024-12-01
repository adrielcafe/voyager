plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.compose.multiplatform)
    id("voyager-kmp-module")
}

android {
    namespace = "cafe.adriel.voyager.navigator.bottomSheet"
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(projects.voyagerCore)
            api(projects.voyagerNavigator)
            compileOnly(compose.runtime)
            compileOnly(compose.material)
        }
        jvmTest.dependencies {
            implementation(libs.junit.api)
            runtimeOnly(libs.junit.engine)
        }
        androidMain.dependencies {
            implementation(libs.androidx.activity.compose)
        }
    }
}
