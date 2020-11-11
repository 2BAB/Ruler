import me.xx2bab.ruler.buildscript.BC.Deps
import me.xx2bab.ruler.buildscript.implementation

plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("android.extensions")
}

android {
    compileSdkVersion(30)
    defaultConfig {
        minSdkVersion(21)
        targetSdkVersion(30)
        versionCode = 1
        versionName = "1.0"
    }
    buildTypes {
        getByName("debug") {
            isMinifyEnabled = false
        }
    }

    lintOptions {
        isAbortOnError = false
    }

    sourceSets["main"].java.srcDir("src/main/kotlin")
}

dependencies {
    implementation(Deps.kotlinGroup)
}