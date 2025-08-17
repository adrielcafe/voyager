import com.vanniktech.maven.publish.MavenPublishBaseExtension
import extensions.isMultiplatformModule
import extensions.kotlinMultiplatform

pluginManager.apply("com.vanniktech.maven.publish")

if (isMultiplatformModule()) {
    kotlinMultiplatform {
        androidTarget {
            publishLibraryVariants("release")
        }
    }
}

group = "cafe.adriel.voyager"

configure<MavenPublishBaseExtension> {
    publishToMavenCentral(automaticRelease = true)
    signAllPublications()

    pom {
        description = "A pragmatic navigation library for Jetpack Compose"
        inceptionYear = "2021"
        url = "https://github.com/adrielcafe/voyager"

        licenses {
            license {
                name = "The MIT License"
                url = "https://opensource.org/licenses/MIT"
                distribution = "repo"
            }
        }

        scm {
            url = "https://github.com/adrielcafe/voyager"
            connection = "scm:git:ssh://git@github.com/adrielcafe/voyager.git"
            developerConnection = "scm:git:ssh://git@github.com/adrielcafe/voyager.git"
        }

        developers {
            developer {
                id = "adrielcafe"
                name = "Adriel Cafe"
                url = "https://github.com/adrielcafe/"
            }
        }
    }
}
