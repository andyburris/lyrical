plugins {
    kotlin("multiplatform")
    alias(libs.plugins.kotlin.serialization)
}

repositories {
    mavenCentral()
//    mavenCentral()
//    maven(url = "https://jitpack.io")
//    maven("https://kotlin.bintray.com/kotlin-js-wrappers/")
}


kotlin {
    jvm()
    js(IR) {
        browser {}
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(kotlin("stdlib-common"))
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.0-native-mt")
                implementation ("org.jetbrains.kotlinx:kotlinx-serialization-json:1.2.0")
                implementation("com.adamratzman:spotify-api-kotlin-core:3.8.8")
                implementation("io.ktor:ktor-client-core:2.3.0")
                implementation("io.ktor:ktor-client-serialization:2.3.0")
                implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.0")
                implementation("io.ktor:ktor-client-content-negotiation:2.3.0")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }
        val jvmMain by getting {
            dependencies {
                implementation("it.skrape:skrape-it:1.0.0-alpha7")
            }
        }

        val jvmTest by getting {
            dependencies {
                implementation("org.jetbrains.kotlin:kotlin-test-junit:1.4.21")
            }
        }

        val jsMain by getting {
            dependencies {
                implementation(kotlin("stdlib-js"))
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-js:1.7.0-RC")
            }
        }

        val jsTest by getting {}
    }
}

configurations.all {
    resolutionStrategy {
        force("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.0-native-mt")
    }
}