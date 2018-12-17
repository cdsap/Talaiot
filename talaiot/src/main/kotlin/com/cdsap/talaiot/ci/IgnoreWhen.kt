package com.cdsap.talaiot.ci

import org.gradle.api.Project

class IgnoreWhen(private val project: Project) {
    var envName: String = ""
    var envValue: String = ""

    fun shouldIgnore() = !envName.isEmpty() && project.hasProperty(envName) &&
            project.property(envName) != null && project.property(envName).toString() == envValue

}