plugins {
    kotlin("js")
    kotlin("plugin.serialization")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib-js"))

    //React, React DOM + Wrappers (chapter 3)
    implementation("com.github.mpetuska:khakra:0.1.1")
    implementation("org.jetbrains.kotlin-wrappers:kotlin-react:18.2.0-pre.541")
    /*implementation("org.jetbrains.kotlin-wrappers:kotlin-react-dom:16.13.1-pre.110-kotlin-1.4.0")*/
    implementation("org.jetbrains.kotlin-wrappers:kotlin-react-router-dom:6.3.0-pre.334")
/*    implementation(npm("react", "16.13.1"))
    implementation(npm("react-dom", "16.13.1"))*/

    //Kotlin Styled (chapter 3)
    implementation("org.jetbrains.kotlin-wrappers:kotlin-styled:5.3.9-pre.541")
    implementation(npm("styled-components", "~5.2.0"))
    implementation(npm("inline-style-prefixer", "~6.0.0"))


    implementation("com.adamratzman:spotify-api-kotlin-core:3.8.8")
    implementation(project(":shared"))
}

kotlin {
    js {
        useCommonJs()
        binaries.executable()
        browser {
            commonWebpackConfig {
                cssSupport {
                    enabled.set(true)
                }
            }
        }
    }
}