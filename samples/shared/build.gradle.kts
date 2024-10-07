

plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("org.jetbrains.compose")
}

setupModuleForComposeMultiplatform(
    fullyMultiplatform = true,
    withKotlinExplicitMode = false
)

android {
    namespace = "cafe.adriel.voyager.sample.shared"
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(compose.material)
            implementation(compose.ui)
            implementation(compose.runtime)

            implementation(projects.voyagerCore)
            implementation(projects.voyagerNavigator)
            implementation(projects.voyagerScreenmodel)
            implementation(projects.voyagerTabNavigator)
            implementation(projects.voyagerBottomSheetNavigator)
            implementation(projects.voyagerTransitions)
            implementation(projects.voyagerLifecycleKmp)

            implementation(libs.androidxKmp.lifecycle.viewmodelCompose)

            implementation(libs.coroutines.core)
        }

        androidMain.dependencies {
            implementation(libs.appCompat)
        }
    }
}
