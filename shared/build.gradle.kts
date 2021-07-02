plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    id("org.jetbrains.kotlin.native.cocoapods")
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
    js(IR) {
        browser {}
    }
    val sdkName: String? = System.getenv("SDK_NAME")
    val isiOSDevice = sdkName.orEmpty().startsWith("iphoneos")
    if (isiOSDevice) {
        iosArm64("iOS")
    } else {
        iosX64("iOS")
    }

    cocoapods {
        // Configure fields required by CocoaPods.
        summary = "Some description for a Kotlin/Native module"
        homepage = "Link to a Kotlin/Native module homepage"
    }


    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(kotlin("stdlib-common"))
                implementation(Dependencies.Coroutines.core)
                implementation (Dependencies.serialization)
                implementation(Dependencies.spotify)
                implementation(Dependencies.Ktor.client)
                implementation(Dependencies.Ktor.clientAuth)
                implementation(Dependencies.Ktor.clientSerialization)
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
                implementation(Dependencies.Coroutines.js)
            }
        }

        val jsTest by getting {}

        val iOSMain by getting {
        }
        val iOSTest by getting{}
    }
}