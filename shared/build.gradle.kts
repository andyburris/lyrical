plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
}

group = "com.andb.com.andb.apps"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    jcenter()
    maven(url = "https://jitpack.io")
    maven("https://kotlin.bintray.com/kotlin-js-wrappers/")
}


kotlin {
    jvm()
    js(BOTH) {
        browser {}
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(kotlin("stdlib-common"))
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.4.2")
                implementation ("org.jetbrains.kotlinx:kotlinx-serialization-json:1.0.1")
                implementation("com.adamratzman:spotify-api-kotlin-core:3.6.0-rc.2")
                implementation("io.ktor:ktor-client-core:1.5.0")
                implementation("io.ktor:ktor-client-serialization:1.5.0")
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
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-js:1.4.2")
            }
        }

        val jsTest by getting {}
    }
}