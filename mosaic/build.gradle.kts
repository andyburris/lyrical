plugins {
    kotlin("jvm")
    id("com.jakewharton.mosaic")
    id("application")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("org.jline:jline:3.17.1")
    implementation(project(":shared"))
}
