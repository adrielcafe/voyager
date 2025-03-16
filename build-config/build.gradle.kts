plugins {
    `kotlin-dsl`
}

dependencies {
    compileOnly(libs.pluginartifact.android)
    compileOnly(libs.pluginartifact.kotlin)
    compileOnly(libs.pluginartifact.ktlint)
    compileOnly(libs.pluginartifact.mavenPublish)
}
