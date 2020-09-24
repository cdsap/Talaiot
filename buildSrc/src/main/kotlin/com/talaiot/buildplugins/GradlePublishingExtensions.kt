package com.talaiot.buildplugins

import com.gradle.publish.PluginBundleExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.invoke
import org.gradle.plugin.devel.GradlePluginDevelopmentExtension

fun Project.setUpGradlePublishing(){

    configure<GradlePluginDevelopmentExtension> {
        plugins {
            register(project.name) {
                val extension = project.extensions.getByType<TalaiotPluginConfiguration>()
                id = extension.idPlugin
                implementationClass = extension.mainClass
            }
        }
    }

    configure<PluginBundleExtension>() {
        (plugins){
            (name){
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
