buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
//        classpath("com.android.tools.build:gradle:8.0.0")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.9.10")
//        classpath("org.jetbrains.kotlin:kotlin-serialization:1.8.20")
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
        maven(url = "https://dl.bintray.com/ekito/koin")
        maven(url = "https://jitpack.io")
        maven("https://us-central1-maven.pkg.dev/varabyte-repos/public")
    }
}
