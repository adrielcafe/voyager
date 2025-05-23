plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.multiplatform)
    id("voyager-jvm-module")
}

android {
    namespace = "cafe.adriel.voyager.rxjava"
}

kotlin {
    sourceSets {
        commonJvmMain.dependencies {
            api(projects.voyagerCore)
            api(projects.voyagerScreenmodel)
            compileOnly(libs.rxjava)
        }
    }
}
