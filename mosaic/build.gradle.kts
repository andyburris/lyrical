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
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.0")
    implementation(Dependencies.Ktor.client)
    implementation(project(":shared"))
}

application {
    mainClassName = "AppKt"
}
