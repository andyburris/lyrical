plugins {
    kotlin("jvm")
    application
    alias(libs.plugins.kotlin.serialization)
}

dependencies {
    implementation(libs.kotlinx.coroutines.core)

    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.cors)
    implementation(libs.ktor.server.netty)
    implementation(libs.ktor.server.serialization)
    implementation(libs.ktor.server.websockets)
    implementation(libs.ktor.server.contentnegotiation)
    implementation(libs.ktor.serialization.kotlinxjson)
    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.contentnegotiation)

    implementation("ch.qos.logback:logback-classic:1.2.9")

    implementation(libs.kotlinx.serialization.json) // JVM dependency

    implementation(project(":shared"))
}

application {
    mainClass.set("com.andb.apps.lyricalbackend.ServerKt")
}

task("stage") {
    dependsOn("installDist")
}