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
import org.gradle.kotlin.dsl.hasPlugin
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmOptions
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinAndroidTarget
import org.jetbrains.kotlin.gradle.targets.js.dsl.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

private fun BaseExtension.setupAndroid() {
    compileSdkVersion(34)
    defaultConfig {
        minSdk = 21
        targetSdk = 34

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
    enableWasm: Boolean = true,
) {
    plugins.withType<org.jetbrains.kotlin.gradle.plugin.KotlinBasePluginWrapper> {
        extensions.configure<KotlinMultiplatformExtension> {
            if (withKotlinExplicitMode) {
                explicitApi()
            }
            sourceSets {
                all {
                    languageSettings.optIn("cafe.adriel.voyager.core.annotation.InternalVoyagerApi")
                    languageSettings.optIn("cafe.adriel.voyager.core.annotation.ExperimentalVoyagerApi")
                }
            }

            applyDefaultHierarchyTemplate {
                common {
                    if(fullyMultiplatform) {
                        group("commonWeb") {
                            withJs()
                            if(enableWasm) {
                                withWasm()
                            }
                        }
                    }
                    group("jvm") {
                        withCompilations {
                            it.target.targetName == "desktop" || it.target is KotlinAndroidTarget
                        }
                    }
                }
            }

            androidTarget {
                if (plugins.hasPlugin("com.vanniktech.maven.publish")) {
                    publishLibraryVariants("release")
                }
            }
            jvm("desktop")

            if (fullyMultiplatform) {
                js(IR) {
                    browser()
                }
                if (enableWasm) {
                    @OptIn(ExperimentalWasmDsl::class)
                    wasmJs { browser() }
                }
                macosX64()
                macosArm64()
                iosArm64()
                iosX64()
                iosSimulatorArm64()
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
    freeCompilerArgs += "-opt-in=cafe.adriel.voyager.core.annotation.InternalVoyagerApi"
    freeCompilerArgs += "-opt-in=cafe.adriel.voyager.core.annotation.ExperimentalVoyagerApi"
}

private fun Project.findAndroidExtension(): BaseExtension = extensions.findByType<LibraryExtension>()
    ?: extensions.findByType<com.android.build.gradle.AppExtension>()
    ?: error("Could not found Android application or library plugin applied on module $name")
