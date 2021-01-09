plugins {
    kotlin("multiplatform") version "1.4.21"
    kotlin("plugin.serialization") version "1.4.21"
    //kotlin("js") version "1.4.21"
}

group = "com.andb.apps"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    jcenter()
    maven(url = "https://jitpack.io")
    maven("https://kotlin.bintray.com/kotlin-js-wrappers/")
}

kotlin {

    jvm()
    js(LEGACY) {
        browser {
            binaries.executable()
            webpackTask {
                cssSupport.enabled = true
            }
            runTask {
                cssSupport.enabled = true
            }
            testTask {
                useKarma {
                    useChromeHeadless()
                    webpackConfig.cssSupport.enabled = true
                }
            }
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(kotlin("stdlib-common"))
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.4.2")
                implementation ("org.jetbrains.kotlinx:kotlinx-serialization-json:1.0.1")
                implementation("com.adamratzman:spotify-api-kotlin-core:3.4.01")
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

        val jsMain by getting {
            dependencies {
                implementation(kotlin("stdlib-js"))

                //React, React DOM + Wrappers (chapter 3)
                implementation("org.jetbrains:kotlin-react:16.13.1-pre.110-kotlin-1.4.0")
                implementation("org.jetbrains:kotlin-react-dom:16.13.1-pre.110-kotlin-1.4.0")
                implementation(npm("react", "16.13.1"))
                implementation(npm("react-dom", "16.13.1"))

                //Kotlin Styled (chapter 3)
                implementation("org.jetbrains:kotlin-styled:5.2.0-pre.125-kotlin-1.4.10")
                implementation(npm("styled-components", "~5.1.1"))
                //implementation(npm("react-is", "~16.8.0"))
                implementation(npm("inline-style-prefixer", "~6.0.0"))
            }
        }
    }
}