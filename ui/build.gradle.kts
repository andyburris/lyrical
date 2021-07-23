plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose") version "0.5.0-build262"
    //id("com.android.library")
}

version = "1.0.0"

repositories {
    mavenCentral()
}

/*
android {

}
*/

kotlin {
    //android()
    js(IR) {
        browser {}
        binaries.executable()
    }
    jvm()

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(kotlin("stdlib-common"))
                implementation(Dependencies.Coroutines.core)
                implementation(Dependencies.Ktor.client)
                implementation(compose.runtime)
                implementation(compose.web.widgets)
                implementation(project(":shared"))
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
                implementation(compose.desktop.currentOs)
            }
        }
        val jvmTest by getting {

        }

/*        val androidMain by getting {

        }
        val androidTest by getting {

        }*/

        val jsMain by getting {
            dependencies {
                implementation(compose.web.core)
            }
        }
        val jsTest by getting {

        }
    }
}