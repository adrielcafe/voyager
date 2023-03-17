import com.android.build.gradle.BaseExtension
import com.android.build.gradle.LibraryExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.plugins.ExtensionAware
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.creating
import org.gradle.kotlin.dsl.findByType
import org.gradle.kotlin.dsl.get
import org.gradle.kotlin.dsl.getValue
import org.gradle.kotlin.dsl.getting
import org.gradle.kotlin.dsl.invoke
import org.gradle.kotlin.dsl.withType
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmOptions
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

private fun BaseExtension.setupAndroid() {
    compileSdkVersion(33)
    defaultConfig {
        minSdk = 21
        targetSdk = 33

        versionCode = 1
        versionName = "1.0"
    }
}

fun Project.setupModuleForAndroidxCompose(
    withKotlinExplicitMode: Boolean = true,
) {
    val androidExtension: BaseExtension = extensions.findByType<LibraryExtension>()
        ?: extensions.findByType<com.android.build.gradle.AppExtension>()
        ?: error("Could not found Android application or library plugin applied on module $name")

    val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")
    val composeCompilerVersion = libs.findVersion("composeCompiler").get().requiredVersion

    androidExtension.apply {
        setupAndroid()

        buildFeatures.apply {
            compose = true
        }

        composeOptions {
            kotlinCompilerExtensionVersion = composeCompilerVersion
        }

        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_1_8
            targetCompatibility = JavaVersion.VERSION_1_8
        }

        testOptions {
            unitTests.all {
                it.useJUnitPlatform()
            }
        }

        (this as ExtensionAware).extensions.configure<KotlinJvmOptions> {
            configureKotlinJvmOptions(withKotlinExplicitMode)
        }
    }
}

fun Project.setupModuleForComposeMultiplatform(
    withKotlinExplicitMode: Boolean = true,
    fullyMultiplatform: Boolean = false,
    iosPrefixName: String = "ios" // only used in ios sample
) {
    plugins.withType<org.jetbrains.kotlin.gradle.plugin.KotlinBasePluginWrapper> {
        extensions.configure<KotlinMultiplatformExtension> {
            if (withKotlinExplicitMode) {
                explicitApi()
            }

            android {
                publishAllLibraryVariants()
            }
            jvm("desktop")

            if (fullyMultiplatform) {
                js(IR) {
                    browser()
                }
                macosX64()
                macosArm64()
                ios(iosPrefixName)
                iosSimulatorArm64("${iosPrefixName}SimulatorArm64")
            }

            sourceSets {
                /* Source sets structure
                common
                  ├─ jvm
                      ├─ android
                      ├─ desktop
                 */
                val commonMain by getting
                val commonTest by getting
                val jvmMain by creating {
                    dependsOn(commonMain)
                }
                val jvmTest by creating {
                    dependsOn(commonTest)
                }


                val desktopMain by getting {
                    dependsOn(jvmMain)
                }
                val androidMain by getting {
                    dependsOn(jvmMain)
                }
                val desktopTest by getting {
                    dependsOn(jvmTest)
                }

                if (fullyMultiplatform) {
                    val nativeMain by creating {
                        dependsOn(commonMain)
                    }

                    val macosMain by creating {
                        dependsOn(nativeMain)
                    }
                    val macosX64Main by getting {
                        dependsOn(macosMain)
                    }
                    val macosArm64Main by getting {
                        dependsOn(macosMain)
                    }
                    val iosMain = getByName(iosPrefixName + "Main").apply {
                        dependsOn(nativeMain)
                    }
                    val iosSimulatorArm64Main = getByName(iosPrefixName + "SimulatorArm64Main").apply {
                        dependsOn(iosMain)
                    }
                }
            }
        }

        findAndroidExtension().apply {
            setupAndroid()
            sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
        }

        tasks.withType<KotlinCompile> {
            kotlinOptions.configureKotlinJvmOptions(withKotlinExplicitMode)
        }
    }
}

private fun KotlinJvmOptions.configureKotlinJvmOptions(
    enableExplicitMode: Boolean
) {
    jvmTarget = JavaVersion.VERSION_1_8.toString()

    if (enableExplicitMode) freeCompilerArgs += "-Xexplicit-api=strict"
}

private fun Project.findAndroidExtension(): BaseExtension = extensions.findByType<LibraryExtension>()
    ?: extensions.findByType<com.android.build.gradle.AppExtension>()
    ?: error("Could not found Android application or library plugin applied on module $name")
