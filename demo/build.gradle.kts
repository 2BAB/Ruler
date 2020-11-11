import me.xx2bab.ruler.buildscript.BC.Deps
import me.xx2bab.ruler.buildscript.implementation

plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("android.extensions")
}

android {
    compileSdkVersion(30)
    defaultConfig {
        applicationId = "me.xx2bab.ruler.demo"
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

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    sourceSets["main"].java.srcDir("src/main/kotlin")

}

dependencies {
    implementation(project(":ruler"))
    implementation(Deps.kotlinGroup)
    implementation(Deps.jetpackUIGroup)
    implementation(Deps.jetpackLifeCycleRuntimeGroup)
}
