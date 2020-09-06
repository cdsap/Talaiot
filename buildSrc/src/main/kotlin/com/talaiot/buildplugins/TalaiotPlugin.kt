package com.talaiot.buildplugins

import com.gradle.publish.PluginBundleExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.*
import org.gradle.plugin.devel.GradlePluginDevelopmentExtension
import org.gradle.testing.jacoco.plugins.JacocoPluginExtension
import org.gradle.testing.jacoco.tasks.JacocoReport

import java.io.File
import java.net.URI

/**
 * Talaiot Plugin abstracts the build logic for modules used as Gradle Plugin.
 * Applies publication Configuration using plugins maven and gradle.
 */
class TalaiotPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        val extension = target
            .extensions
            .create<TalaiotPluginExtension>("talaiotPlugin")

        println(extension)
        target.plugins.apply("java-gradle-plugin")
        target.plugins.apply("maven-publish")
        target.plugins.apply("jacoco")
        target.plugins.apply("kotlin")
        target.plugins.apply("com.gradle.plugin-publish")

        target.repositories {
            jcenter()
            mavenCentral()
        }

        target.dependencies {
            add("testImplementation", "com.nhaarman.mockitokotlin2:mockito-kotlin:2.0.0-RC1")
            add("testImplementation", "io.kotlintest:kotlintest-runner-junit5:3.3.2")
        }

        setupJacoco()
        setUpJunitPlatform()

        target.version = Versions.TALAIOT_VERSION
        target.afterEvaluate {
            collectUnitTest()
            setTalaiotPluginGroup(target)
            setTalaiotPluginVersion(target)
            configGradlePlugin(target)
            setUpPublishing()
        }
    }

    private fun setTalaiotPluginGroup(target: Project) {
        val extension = target.extensions.getByType<TalaiotPluginExtension>()
        target.group = extension.group ?: "com.cdsap.talaiot.plugin"
    }

    private fun setTalaiotPluginVersion(target: Project) {
        val extension = target.extensions.getByType<TalaiotPluginExtension>()
        target.version = extension.version ?: Versions.TALAIOT_VERSION
    }

    private fun configGradlePlugin(project: Project) {

        project.configure<GradlePluginDevelopmentExtension> {
            plugins {
                register(project.name) {
                    val extension = project.extensions.getByType<TalaiotPluginExtension>()
                    id = extension.idPlugin
                    implementationClass = extension.mainClass
                }
            }
        }

        project.configure<PluginBundleExtension>() {
            (plugins){
                (project.name){
                    val extension = project.extensions.getByType<TalaiotPluginExtension>()
                    displayName = project.name
                    description =
                        "Simple and extensible plugin to track task and build times in your Gradle Project."
                    tags = listOf("tracking", "kotlin", "gradle")
                    version = Versions.TALAIOT_VERSION
                }
            }
        }
    }
}
