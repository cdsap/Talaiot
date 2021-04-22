package com.talaiot.buildplugins

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.jvm.toolchain.JavaLanguageVersion
import org.gradle.kotlin.dsl.*
import org.gradle.plugins.signing.SigningExtension
import java.net.URI

/**
 * Talaiot Plugin abstracts the build logic for modules used as Gradle Plugin.
 * Applies publication Configuration using plugins maven and gradle.
 */
class TalaiotPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        target
            .extensions
            .create<TalaiotPluginConfiguration>("talaiotPlugin")

        target.plugins.apply("java-gradle-plugin")
        target.plugins.apply("maven-publish")
        target.plugins.apply("jacoco")
        target.plugins.apply("signing")
        target.plugins.apply("kotlin")
        target.plugins.apply("java-library")
        target.plugins.apply("application")
        target.plugins.apply("com.gradle.plugin-publish")

        target.repositories {
            mavenCentral()
            maven { url = URI("https://plugins.gradle.org/m2/") }
        }

        target.setUpKotlinCompiler()
        target.setUpJacoco()
        target.setUpJunitPlatform()

        target.afterEvaluate {
            val extension = target.extensions.getByType<TalaiotPluginConfiguration>()
            setProjectVersion(extension.version)
            setProjectGroup(extension.group, Type.PLUGIN)
            setUpGradlePublishing()
            setUpPublishing(Type.PLUGIN)
            collectUnitTest()
            setUpSigning("TalaiotLib", "pluginMaven")
        }

        target.dependencies {
            add("testImplementation", "com.nhaarman.mockitokotlin2:mockito-kotlin:2.0.0-RC1")
            add("testImplementation", "io.kotlintest:kotlintest-runner-junit5:3.3.2")
        }
    }
}

