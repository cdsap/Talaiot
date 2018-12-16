package com.cdsap.talaiot.ci

import org.gradle.api.Project

class CiConfiguration(val project: Project) {
    private var execute = false
    private var envName: String = ""
    private var envValue: String = ""

    private fun isCiBuild() = !envName.isEmpty() && project.hasProperty(envName) &&
            project.property(envName) != null && project.property(envName).toString() == envValue

    fun executeOnCi() = execute || isCiBuild()

}