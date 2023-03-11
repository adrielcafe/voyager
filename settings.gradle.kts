enableFeaturePreview("VERSION_CATALOGS")
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

dependencyResolutionManagement {
    // repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven(url = "https://maven.pkg.jetbrains.space/public/p/compose/dev" )
    }
}

plugins {
    id("com.dropbox.focus") version "0.4.0"
}

configure<com.dropbox.focus.FocusExtension> {
    allSettingsFileName.set("includes.gradle.kts")
    focusFileName.set(".focus")
}
