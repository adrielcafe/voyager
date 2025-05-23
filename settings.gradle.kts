import com.dropbox.focus.FocusExtension

rootProject.name = "voyager"

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        mavenCentral()
        google()
        gradlePluginPortal()
    }
    resolutionStrategy {
        eachPlugin {
            if (requested.version == null) return@eachPlugin
            when (requested.id.id) {
                "com.android.tools.build" -> {
                    useModule("${requested.id.id}:gradle:${requested.version}")
                }
            }
        }
    }
}

dependencyResolutionManagement {
//    repositoriesMode = RepositoriesMode.FAIL_ON_PROJECT_REPOS
    repositories {
        google()
        mavenCentral()
        maven(url = "https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
    versionCatalogs {
        create("samplesCatalog") {
            from(files("./gradle/samples.versions.toml"))
        }
    }
}

includeBuild("build-config")

plugins {
    id("com.dropbox.focus") version "0.4.0"
}

configure<FocusExtension> {
    allSettingsFileName.set("includes.gradle.kts")
    focusFileName.set(".focus")
}
