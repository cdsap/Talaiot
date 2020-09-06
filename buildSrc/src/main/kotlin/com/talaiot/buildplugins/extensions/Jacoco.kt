package com.talaiot.buildplugins.extensions

fun Project.setupJacoco() = this.run {
    tasks.withType<JacocoReport> {
        reports {
            xml.isEnabled = true
            csv.isEnabled = true
            html.isEnabled = true
            html.destination = File("${target.rootProject.buildDir}/coverage")
        }
    }
    configure<JacocoPluginExtension> {
        toolVersion = "0.8.3"
    }
}