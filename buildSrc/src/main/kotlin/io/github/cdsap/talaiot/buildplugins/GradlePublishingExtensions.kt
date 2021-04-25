package io.github.cdsap.talaiot.buildplugins

import com.gradle.publish.PluginBundleExtension
import org.gradle.api.Project
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.invoke
import org.gradle.kotlin.dsl.named
import org.gradle.plugin.devel.GradlePluginDevelopmentExtension

fun Project.setUpGradlePublishing() {
    val extension = project.extensions.getByType<TalaiotPluginConfiguration>()

    configure<GradlePluginDevelopmentExtension> {
        plugins {
            register(project.name) {
                id = extension.idPlugin
                implementationClass = extension.mainClass
            }
        }
    }

    configure<PluginBundleExtension>() {
        (plugins){
            (name){
                displayName = extension.displayName
                website = "https://github.com/cdsap/Talaiot"
                vcsUrl = "https://github.com/cdsap/Talaiot"
                description =
                    "${extension.displayName}, simple and extensible plugin to track task and build times in your Gradle Project."
                tags = listOf("tracking", "kotlin", "gradle")
                version = extension.version
            }
        }
    }

    configure<PublishingExtension> {
        publications {
            named<MavenPublication>("pluginMaven") {
                artifactId = extension.artifact
            }
        }
    }
}
