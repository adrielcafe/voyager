plugins {
    id("com.android.application")
    kotlin("android")
}

setupModuleForAndroidxCompose(
    withKotlinExplicitMode = false
)

android {
    namespace = "cafe.adriel.voyager.sample.multimodule"
    defaultConfig {
        applicationId = "cafe.adriel.voyager.sample.multimodule"
    }
    lint {
        // related to https://github.com/bumptech/glide/issues/4940
        lintConfig = file("${project.projectDir}/../../android_leakcanary_lint.xml")
    }
}

dependencies {
    implementation(projects.voyagerNavigator)

    implementation(projects.samples.multiModule.featureHome)
    implementation(projects.samples.multiModule.featurePosts)

    implementation(libs.appCompat)
    implementation(libs.compose.compiler)
    implementation(libs.compose.runtime)
    implementation(libs.compose.activity)
    implementation(libs.compose.material)

    debugImplementation(libs.leakCanary)
}
