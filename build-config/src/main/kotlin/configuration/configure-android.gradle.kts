import com.android.build.gradle.BaseExtension
import extensions.isAndroidApplicationModule
import extensions.isAndroidLibraryModule
import extensions.isMultiplatformModule

configure<BaseExtension> {
    compileSdkVersion(36)
    defaultConfig {
        minSdk = 21

        if (isAndroidApplicationModule()) {
            targetSdk = 36
            versionCode = 1
            versionName = "1.0"
        }

        if (isAndroidLibraryModule()) {
            val proguardFilename = "consumer-rules.pro"
            if (layout.projectDirectory.file(proguardFilename).asFile.exists()) {
                consumerProguardFile(proguardFilename)
            }
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    testOptions {
        unitTests.all(Test::useJUnitPlatform)
    }

    if (isMultiplatformModule()) {
        sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    }
}
