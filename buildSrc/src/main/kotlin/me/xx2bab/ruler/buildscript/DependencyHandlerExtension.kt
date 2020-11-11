package me.xx2bab.ruler.buildscript

import org.gradle.api.artifacts.Dependency
import org.gradle.api.artifacts.dsl.DependencyHandler

fun DependencyHandler.implementation(dependencyNotations: Set<Any>): List<Dependency?> {
    val res = mutableListOf<Dependency?>()
    for (dependencyNotation in dependencyNotations) {
        res.add(add("implementation", dependencyNotation))
    }
    return res
}

fun DependencyHandler.testImplementation(dependencyNotations: Set<Any>): List<Dependency?> {
    val res = mutableListOf<Dependency?>()
    for (dependencyNotation in dependencyNotations) {
        res.add(add("testImplementation", dependencyNotation))
    }
    return res
}

fun DependencyHandler.androidTestImplementation(dependencyNotations: Set<Any>): List<Dependency?> {
    val res = mutableListOf<Dependency?>()
    for (dependencyNotation in dependencyNotations) {
        res.add(add("androidTestImplementation", dependencyNotation))
    }
    return res
}