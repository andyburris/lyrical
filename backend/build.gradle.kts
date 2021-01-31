plugins {
    id("kotlin-platform-jvm")
    application
    kotlin("plugin.serialization")
}

kotlin {

}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.4.2")

    implementation("io.ktor:ktor-server-core:1.5.1")
    implementation("io.ktor:ktor-client-core:1.5.1")
    implementation("io.ktor:ktor-server-netty:1.5.1")
    implementation("io.ktor:ktor-serialization:1.5.1")

    implementation("ch.qos.logback:logback-classic:1.2.3")

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:1.0.1") // JVM dependency
    implementation("io.ktor:ktor-websockets:1.5.1")

    implementation(project(":shared"))
}

application {
    mainClass.set("ServerKt")
}

task("stage") {
    dependsOn("installDist")
}