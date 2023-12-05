plugins {
    id("com.android.application") version libs.versions.applicationPlugin apply false
    id("com.google.android.gms.oss-licenses-plugin") version libs.versions.ossLicensesPlugin apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.ktlint)
    alias(libs.plugins.detekt)
    alias(libs.plugins.hilt.android) apply false
    alias(libs.plugins.kotlin.kapt) apply false
    alias(libs.plugins.devtools.ksp) apply false
}

val projectSource = file(projectDir)
val configFile = files("$rootDir/config/detekt/detekt.yml")
val kotlinFiles = "**/*.kt"
val resourceFiles = "**/resources/**"
val buildFiles = "**/build/**"

apply(plugin = libs.plugins.detekt.asProvider().get().pluginId)

dependencies {
    detektPlugins(libs.plugins.detekt.formatting.get().toString())
    detektPlugins(libs.plugins.compose.rules.detekt.get().toString())
}

tasks.register<io.gitlab.arturbosch.detekt.Detekt>("detektAll") {
    description = "Custom DETEKT build for all modules"
    parallel = true
    ignoreFailures = false
    autoCorrect = true
    buildUponDefaultConfig = true
    setSource(projectSource)
    config.setFrom(configFile)
    include(kotlinFiles)
    exclude(resourceFiles, buildFiles)
    reports {
        html.required.set(true)
        xml.required.set(false)
        txt.required.set(false)
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
