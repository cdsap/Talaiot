package com.talaiot.buildplugins

import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType

fun Project.setUpKotlinCompiler() {
    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
        kotlinOptions {
            freeCompilerArgs = listOf("-Xjvm-default=compatibility")
        }
    }
}