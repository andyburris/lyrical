plugins {
    kotlin("js")
    kotlin("plugin.serialization")
}

dependencies {
    implementation(kotlin("stdlib-js"))

    //React, React DOM + Wrappers (chapter 3)
    implementation("com.github.mpetuska:khakra:0.1.1")
    implementation("org.jetbrains:kotlin-react:17.0.1-pre.148-kotlin-1.4.30")
    /*implementation("org.jetbrains:kotlin-react-dom:16.13.1-pre.110-kotlin-1.4.0")*/
    implementation("org.jetbrains:kotlin-react-router-dom:5.2.0-pre.148-kotlin-1.4.30")
/*    implementation(npm("react", "16.13.1"))
    implementation(npm("react-dom", "16.13.1"))*/

    //Kotlin Styled (chapter 3)
    implementation("org.jetbrains:kotlin-styled:5.2.1-pre.148-kotlin-1.4.30")
    implementation(npm("styled-components", "~5.2.0"))
    //implementation(npm("react-is", "~16.8.0"))
    implementation(npm("inline-style-prefixer", "~6.0.0"))


    implementation("com.adamratzman:spotify-api-kotlin-core:3.6.0-rc.2")
    implementation(project(":shared"))
}

kotlin {
    js {
        useCommonJs()
        binaries.executable()
        browser {
            commonWebpackConfig {
                cssSupport.enabled = true
            }
        }
/*        browser {
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
        binaries.executable()*/
    }
}