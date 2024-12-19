import org.jlleitschuh.gradle.ktlint.KtlintExtension

plugins.apply("org.jlleitschuh.gradle.ktlint")

configure<KtlintExtension> {
    version.set("0.47.1")
    disabledRules.set(setOf("filename"))
}
