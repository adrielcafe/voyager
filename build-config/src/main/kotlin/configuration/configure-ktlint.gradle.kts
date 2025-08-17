import org.jlleitschuh.gradle.ktlint.KtlintExtension

plugins.apply("org.jlleitschuh.gradle.ktlint")

configure<KtlintExtension> {
    val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

    version = libs.findVersion("ktlint").get().toString()
    disabledRules = setOf("filename")
}
