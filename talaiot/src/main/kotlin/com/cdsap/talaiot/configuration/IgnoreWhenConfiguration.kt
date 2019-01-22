package com.cdsap.talaiot.configuration

import org.gradle.api.Project

class IgnoreWhenConfiguration(private val project: Project) {
    var envName: String = ""
    var envValue: String = ""

    fun shouldIgnore() = !envName.isEmpty() && project.hasProperty(envName) &&
            project.property(envName) != null && project.property(envName).toString() == envValue

}