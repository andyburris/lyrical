rootProject.name = "LyricGuesser"
include("backend", "shared", "web", "site")

pluginManagement {
    repositories {
        google()
        mavenCentral()
//        gradlePluginPortal()
        maven("https://us-central1-maven.pkg.dev/varabyte-repos/public")
    }
}
