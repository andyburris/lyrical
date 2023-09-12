rootProject.name = "LyricGuesser"
include("backend", "shared", "site", "mosaic")

pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        maven("https://us-central1-maven.pkg.dev/varabyte-repos/public")
    }
}