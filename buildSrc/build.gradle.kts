plugins {
    `kotlin-dsl`
}

repositories {
    google()
    jcenter()
    mavenCentral()
    maven {
        setUrl("https://plugins.gradle.org/m2/")
    }
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(kotlin("gradle-plugin"))
    // Please sync it with the one in root build.gradle.kts
    implementation("com.android.tools.build:gradle:4.2.0-alpha16")
}