rootProject.name = "LyricGuesser"
include("backend", "shared", "ui", "mosaic", "test")

pluginManagement {
    repositories {
        gradlePluginPortal()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
}