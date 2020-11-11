package me.xx2bab.ruler.buildscript

import org.gradle.api.JavaVersion

// BuildConfig for Gradle Scripts
object BC {

    // How to query versions:
    //
    // - Google Maven Repo: https://maven.google.com/web/index.html
    // - AndroidX releases: https://developer.android.com/jetpack/androidx/versions
    // - JCenter: https://bintray.com/bintray/jcenter
    object Versions {

        // Android SDK Versions
        const val compileSdkVersion = 30
        const val minSdkVersion = 26
        const val targetSdkVersion = 30

        // Java Compatible Version
        const val ktVer = "1.4.10"
        const val ktCoroutineVer = "1.3.7"
        val sourceCompatibilityVersion = JavaVersion.VERSION_1_8
        val targetCompatibilityVersion = JavaVersion.VERSION_1_8

        // Modulization / DI
        const val broVer = "1.3.6"
        const val koinVer = "2.1.6"

        // Jetpack
        const val jectpackAppCompatVer = "1.2.0"
        const val jectpackMaterialVer = "1.2.0"
        const val jetpackArchCoreVer = "2.1.0"
        const val jetpackLifecycleVer = "2.2.0"
        const val jectpackTestVer = "1.3.0-rc01"
        const val jetpackEspressoVer = "3.3.0-rc01"

        // Other 3rd libs
        const val retrofitVer = "2.9.0"
        const val okhttpVer = "4.7.2"

    }

    object Deps {

        // Kotlin
        const val kotlinStd = "org.jetbrains.kotlin:kotlin-stdlib:${Versions.ktVer}"
        const val kotlinCoroutineCore = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.ktCoroutineVer}"
        const val kotlinCoroutineAndroid = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.ktCoroutineVer}"
        val kotlinGroup = setOf(
                kotlinStd,
                kotlinCoroutineCore,
                kotlinCoroutineAndroid
        )

        // Jetpack - UI
        const val jetpackAppCompat = "androidx.appcompat:appcompat:${Versions.jectpackAppCompatVer}"
        const val jetpackMaterial = "com.google.android.material:material:${Versions.jectpackMaterialVer}"
        const val jetpackConstraintLayout = "androidx.constraintlayout:constraintlayout:1.1.3"
        val jetpackUIGroup = setOf(
                jetpackAppCompat,
                jetpackMaterial,
                jetpackConstraintLayout
        )

        // Jetpack - Lifecycle
        const val jetpackLifeCycleRuntime = "androidx.lifecycle:lifecycle-runtime-ktx:${Versions.jetpackLifecycleVer}"
        const val jetpackLifeCycleExt = "androidx.lifecycle:lifecycle-extensions:${Versions.jetpackLifecycleVer}"
        const val jetpackLifeCycleViewModelKtx = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.jetpackLifecycleVer}"
        const val jetpackLifeCycleLiveDataKtx = "androidx.lifecycle:lifecycle-livedata-ktx:${Versions.jetpackLifecycleVer}"
        const val jetpackLifeCycleCompiler = "androidx.lifecycle:lifecycle-compiler:${Versions.jetpackLifecycleVer}"
        val jetpackLifeCycleRuntimeGroup = setOf(
                jetpackLifeCycleRuntime,
                jetpackLifeCycleExt,
                jetpackLifeCycleViewModelKtx,
                jetpackLifeCycleLiveDataKtx
        )

        // Other 3rd libs
        const val retrofit = "com.squareup.retrofit2:retrofit:${Versions.retrofitVer}"
        const val retrofitGsonAdapter = "com.squareup.retrofit2:converter-gson:${Versions.retrofitVer}"
        const val okHttp = "com.squareup.okhttp3:okhttp:${Versions.okhttpVer}"
        const val okHttpLogger = "com.squareup.okhttp3:logging-interceptor:${Versions.okhttpVer}"
        val networkGroup = setOf(
                retrofit,
                retrofitGsonAdapter,
                okHttp,
                okHttpLogger
        )

        // Test

    }

}