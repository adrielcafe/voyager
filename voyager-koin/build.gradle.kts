plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("org.jetbrains.compose")
    id("com.vanniktech.maven.publish")
}

setupModuleForComposeMultiplatform(
    fullyMultiplatform = true,
    enableWasm = false // https://github.com/InsertKoinIO/koin/issues/1634
)

android {
    namespace = "cafe.adriel.voyager.koin"
    defaultConfig {
        consumerProguardFiles("consumer-rules.pro")
    }
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                api(projects.voyagerCore)
                api(projects.voyagerScreenmodel)
                api(projects.voyagerNavigator)

                compileOnly(compose.runtime)
                compileOnly(compose.runtimeSaveable)

                implementation(libs.coroutines.core)
            }
        }
    }
}

dependencies {
    commonMainImplementation(libs.koin.compose) {
        exclude("org.jetbrains.compose.runtime")
    }
}
