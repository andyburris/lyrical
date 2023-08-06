plugins {
    kotlin("jvm")
    application
    alias(libs.plugins.kotlin.serialization)
    id("io.ktor.plugin") version "2.3.0"
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.4.2")

    implementation("io.ktor:ktor-server-core:2.3.0")
    implementation("io.ktor:ktor-client-core:2.3.0")
    implementation("io.ktor:ktor-server-netty:2.3.0")
    implementation("io.ktor:ktor-serialization:2.3.0")
    implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.0")

    implementation("ch.qos.logback:logback-classic:1.2.9")

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:1.0.1") // JVM dependency
    implementation("io.ktor:ktor-server-websockets:2.3.0")
    implementation("io.ktor:ktor-client-content-negotiation:2.3.0")
    implementation("io.ktor:ktor-server-content-negotiation:2.3.0")
    implementation("io.ktor:ktor-server-cors:2.3.0")

    implementation(project(":shared"))
}

application {
    mainClass.set("com.andb.apps.lyricalbackend.ServerKt")
}

task("stage") {
    dependsOn("installDist")
}