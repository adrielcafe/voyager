import org.gradle.api.internal.catalog.DelegatingProjectDependency

plugins {
    alias(libs.plugins.android.gradle) apply false
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.multiplatform) apply false
    alias(libs.plugins.kotlin.parcelize) apply false
    alias(libs.plugins.kotlin.kapt) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.kotlin.atomicfu) apply false
    alias(libs.plugins.compose.multiplatform) apply false
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.ktlint) apply false
    alias(libs.plugins.mavenPublish) apply false
    alias(libs.plugins.binaryCompatibilityValidator)
}

apiValidation {
    with(ignoredProjects) {
        add(projects.samples.android)
        add(projects.samples.multiplatform)
        add(projects.samples.multiModule.app)
        add(projects.samples.multiModule.featureHome)
        add(projects.samples.multiModule.featurePosts)
        add(projects.samples.multiModule.navigation)
    }
    with(nonPublicMarkers) {
        add("cafe.adriel.voyager.core.annotation.InternalVoyagerApi")
    }
}

fun MutableSet<String>.add(dependency: DelegatingProjectDependency) = add(dependency.name)
