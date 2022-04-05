import com.vanniktech.maven.publish.MavenPublishPluginExtension
import com.vanniktech.maven.publish.SonatypeHost

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
    }
}

subprojects {
    apply(plugin = "org.jlleitschuh.gradle.ktlint")
    plugins.withId("com.vanniktech.maven.publish") {
        configure<MavenPublishPluginExtension> {
            sonatypeHost = SonatypeHost.S01
        }
    }
}

tasks.register<Delete>("clean") {
    delete(rootProject.buildDir)
}
