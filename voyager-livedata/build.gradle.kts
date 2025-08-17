plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    id("voyager-android-module")
}

android {
    namespace = "cafe.adriel.voyager.livedata"
}

dependencies {
    api(projects.voyagerCore)
    api(projects.voyagerScreenmodel)

    implementation(libs.androidx.lifecycle.livedata)

    testRuntimeOnly(libs.junit.engine)
    testImplementation(libs.junit.api)
}
