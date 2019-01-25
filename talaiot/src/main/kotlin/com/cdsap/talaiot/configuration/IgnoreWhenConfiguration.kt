package com.cdsap.talaiot.configuration

import org.gradle.api.Project

class IgnoreWhenConfiguration(private val project: Project) {
    var envName: String = ""
    var envValue: String = ""

    fun shouldIgnore(): Boolean {
        return if (!envName.isEmpty()) {
            if (System.getenv(envName) != null) {
                System.getenv(envName) == envValue
            } else {
                (project.hasProperty(envName) && project.property(envName) != null) && project.property(envName).toString() == envValue
            }
        } else {
            false
        }
    }
}