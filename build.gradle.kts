import java.nio.file.Paths

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
}

tasks.register<Delete>("clean") {
    delete(rootProject.buildDir)
}

/**
 * ===========================================================================================
 * BEGIN SECTION USING GRADLE TO BUILD, TRANSFORM AND INSTALL APP BUNDLE WITH DYNAMIC FEATURE
 * SUPPORT TO LOCAL TESTING. On your terminal just run:
 *
 * ./gradlew runDynamicFeatureSample
 *
 * ===========================================================================================
 */

val appBundleOriginPath = Paths.get("$rootDir","samples", "dynamic-feature", "app", "build", "outputs", "bundle", "debug")
val appBundleDestPath = Paths.get("${rootProject.buildDir}", "app-debug.aab")
val apksPath = Paths.get("${rootProject.buildDir}", "app.apks")
val bundleToolPath = Paths.get("${rootProject.buildDir}", "bundletool.jar")

tasks.register<Copy>("copyAab") {
    dependsOn(":samples:dynamic-feature:app:bundleDebug")

    from(appBundleOriginPath)
    into(rootProject.buildDir)
    include("**/*.aab")
}

tasks.register("downloadBundleTool") {
    dependsOn("copyAab")

    doLast {
        val dest = bundleToolPath.toFile()
        if (!dest.exists()) {
            ant.invokeMethod("get", mapOf(
                "src" to "https://github.com/google/bundletool/releases/download/1.13.2/bundletool-all-1.13.2.jar",
                "dest" to dest
            ))
        }
    }
}

tasks.register<Delete>("cleanApks") {
    delete(apksPath)
}

tasks.register<Exec>("generateApks") {
    dependsOn("cleanApks", "downloadBundleTool")

    executable("java")

    args("-jar", "$bundleToolPath")
    args("build-apks", "--local-testing")
    args("--bundle", "$appBundleDestPath")
    args("--output", "$apksPath")
}

tasks.register<Exec>("runDynamicFeatureSample") {
    dependsOn("generateApks")

    executable("java")

    args("-jar", "$bundleToolPath")
    args("install-apks")
    args("--apks", "$apksPath")
}
