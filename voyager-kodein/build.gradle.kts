plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.compose.multiplatform)
    id("voyager-kmp-module")
}

android {
    namespace = "cafe.adriel.voyager.kodein"
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(projects.voyagerCore)
            api(projects.voyagerScreenmodel)
            api(projects.voyagerNavigator)
            compileOnly(compose.runtime)
            compileOnly(compose.runtimeSaveable)
            compileOnly(libs.kodein)
        }
        jvmTest.dependencies {
            implementation(libs.junit.api)
            runtimeOnly(libs.junit.engine)
        }
    }
}
