rootProject.name = "LyricGuesser"
include("backend", "shared", "web", "ui")

pluginManagement {
    repositories {
        gradlePluginPortal()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
}
include("mosaic")
include("test")
