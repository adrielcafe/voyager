buildscript {
    repositories {
        mavenCentral()
        google()
        gradlePluginPortal()
        maven(url = "https://maven.pkg.jetbrains.space/public/p/compose/dev" )
    }

    dependencies {
        classpath(libs.plugin.hilt)
        classpath(libs.plugin.ktlint)
        classpath(libs.plugin.maven)
        classpath(libs.plugin.multiplatform.compose)
        classpath(libs.plugin.atomicfu)
    }
}

plugins {
    alias(libs.plugins.binaryCompatibilityValidator)
}

subprojects {
    apply(plugin = "org.jlleitschuh.gradle.ktlint")

    configure<org.jlleitschuh.gradle.ktlint.KtlintExtension> {
        version.set("0.47.1")
        disabledRules.set(setOf("filename"))
    }
}

apiValidation {
    ignoredProjects.addAll(listOf(
        /*samples*/"android",
        /*samples*/"multiplatform",
        /*samples/multi-modulo*/"app",
        /*samples/multi-modulo*/"feature-home",
        /*samples/multi-modulo*/"feature-posts",
        /*samples/multi-modulo*/"navigation",
    ))
    nonPublicMarkers.addAll(listOf(
        "cafe.adriel.voyager.core.annotation.InternalVoyagerApi",
        "cafe.adriel.voyager.core.annotation.ExperimentalVoyagerApi"
    ))
}
