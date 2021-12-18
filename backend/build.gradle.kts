plugins {
    id("kotlin-platform-jvm")
    application
    kotlin("plugin.serialization")
}

kotlin {

}

dependencies {
    implementation(Dependencies.Coroutines.core)
    implementation(Dependencies.serialization)
    implementation(Dependencies.Ktor.server)
    implementation(Dependencies.Ktor.netty)
    implementation(Dependencies.Ktor.serverSerialization)
    implementation(Dependencies.Ktor.serverAuth)
    implementation(Dependencies.Ktor.serverAuthJWT)
    implementation(Dependencies.Ktor.websockets)
    implementation(Dependencies.Ktor.clientSerialization)
    implementation(Dependencies.spotify)

    implementation("ch.qos.logback:logback-classic:1.2.3")

    implementation("io.lettuce:lettuce-core:6.1.2.RELEASE")

    implementation(project(":shared"))
}

application {
    mainClass.set("ServerKt")
}

task("stage") {
    dependsOn("installDist")
}