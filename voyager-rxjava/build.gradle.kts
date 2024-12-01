plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.multiplatform)
    id("voyager-desktop-module")
}

android {
    namespace = "cafe.adriel.voyager.rxjava"
}

kotlin {
    sourceSets {
        jvmMain.dependencies {
            api(projects.voyagerCore)
            api(projects.voyagerScreenmodel)
            compileOnly(libs.rxjava)
        }
        jvmTest.dependencies {
            implementation(libs.junit.api)
            runtimeOnly(libs.junit.engine)
        }
    }
}
