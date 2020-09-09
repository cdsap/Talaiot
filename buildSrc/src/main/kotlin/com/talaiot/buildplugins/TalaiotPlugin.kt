package com.talaiot.buildplugins

import com.gradle.publish.PluginBundleExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.*
import org.gradle.plugin.devel.GradlePluginDevelopmentExtension

/**
 * Talaiot Plugin abstracts the build logic for modules used as Gradle Plugin.
 * Applies publication Configuration using plugins maven and gradle.
 */
class TalaiotPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        val extension = target
            .extensions
            .create<TalaiotPluginConfiguration>("talaiotPlugin")

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
            add("testImplementation", project(":library:talaiot-test-utils"))
        }

        target.setUpJacoco()
        target.setUpJunitPlatform()

        target.afterEvaluate {
            val extension = extensions.getByType<TalaiotPluginConfiguration>()
            setProjectVersion(extension.version)
            setProjectGroup(extension.group, Constants.DEFAULT_GROUP_PLUGIN)
            collectUnitTest()
            setUpPublishing("mavenTalaiot", extension.artifact)
            configGradlePlugin(target)
        }
    }

    private fun configGradlePlugin(project: Project) {

        project.configure<GradlePluginDevelopmentExtension> {
            plugins {
                register(project.name) {
                    val extension = project.extensions.getByType<TalaiotPluginConfiguration>()
                    id = extension.idPlugin
                    implementationClass = extension.mainClass
                }
            }
        }

        project.configure<PluginBundleExtension>() {
            (plugins){
                (project.name){
                    val extension = project.extensions.getByType<TalaiotPluginConfiguration>()
                    displayName = project.name
                    description =
                        "Simple and extensible plugin to track task and build times in your Gradle Project."
                    tags = listOf("tracking", "kotlin", "gradle")
                    version = Constants.TALAIOT_VERSION
                }
            }
        }
    }
}
