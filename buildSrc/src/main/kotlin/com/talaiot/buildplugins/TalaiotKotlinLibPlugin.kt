package com.talaiot.buildplugins

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.project
import org.gradle.kotlin.dsl.repositories

/**
 * TalaiotKotlinLib Plugin represents a build configuration
 * of java-libraries]/kotlin modules.
 * These modules will require common build logic for unit tests and jacoco
 */
class TalaiotKotlinLibPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        target.plugins.apply("kotlin")
        target.plugins.apply("maven-publish")
        target.plugins.apply("jacoco")
        target.plugins.apply("java-library")

        target.repositories {
            jcenter()
            mavenCentral()
        }

        target.setUpJacoco()
        target.setUpJunitPlatform()

        target.afterEvaluate {
            setProjectVersion()
            setProjectGroup(Constants.DEFAULT_GROUP_LIBRARY)
            collectUnitTest()
            setUpPublishing("mavenTalaiotLib", null)
        }

        target.dependencies {
            add("implementation", "org.jetbrains.kotlin:kotlin-stdlib:1.3.72")
            add("testImplementation", "com.nhaarman.mockitokotlin2:mockito-kotlin:2.0.0-RC1")
            add("testImplementation", "io.kotlintest:kotlintest-runner-junit5:3.3.2")
        }
    }
}
