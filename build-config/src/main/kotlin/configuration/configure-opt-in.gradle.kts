import org.jetbrains.kotlin.gradle.dsl.KotlinProjectExtension

configure<KotlinProjectExtension> {
    sourceSets.all {
        languageSettings {
            optIn("cafe.adriel.voyager.core.annotation.InternalVoyagerApi")
            optIn("cafe.adriel.voyager.core.annotation.ExperimentalVoyagerApi")
        }
    }
}
