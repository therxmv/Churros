import io.gitlab.arturbosch.detekt.extensions.DetektExtension

plugins {
    // this is necessary to avoid the plugins to be loaded multiple times
    // in each subproject's classloader
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.androidMultiplatformLibrary) apply false
    alias(libs.plugins.composeMultiplatform) apply false
    alias(libs.plugins.composeCompiler) apply false
    alias(libs.plugins.detekt)
    alias(libs.plugins.kotlinMultiplatform) apply false
    alias(libs.plugins.kotlinSerialization) apply false
    alias(libs.plugins.roborazzi) apply false
}

configure<DetektExtension> {
    toolVersion = libs.versions.detekt.get()
    config.setFrom(files("detekt.yml"))
    buildUponDefaultConfig = false
    allRules = false
    source.setFrom(
        "shared/src",
        "androidApp/src",
    )
}