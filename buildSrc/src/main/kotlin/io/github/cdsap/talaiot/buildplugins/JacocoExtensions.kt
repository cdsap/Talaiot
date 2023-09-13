package io.github.cdsap.talaiot.buildplugins

import org.gradle.api.Project
import org.gradle.testing.jacoco.tasks.JacocoReport
import org.gradle.kotlin.dsl.*
import org.gradle.testing.jacoco.plugins.JacocoPluginExtension
import java.io.File

fun Project.setUpJacoco() = this.run {
    configure<JacocoPluginExtension> {
        toolVersion = "0.8.7"
    }

    tasks.withType<JacocoReport> {
        reports {
            html.isEnabled = true
            html.destination = File("${rootProject.buildDir}/coverage")
            xml.isEnabled = true
        }
    }

}
