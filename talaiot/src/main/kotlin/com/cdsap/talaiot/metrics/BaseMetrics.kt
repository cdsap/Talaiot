package com.cdsap.talaiot.metrics

import org.gradle.BuildResult
import org.gradle.internal.os.OperatingSystem

class BaseMetrics(private val result: BuildResult) : Metrics {

    override fun get() = mapOf(
        "user" to System.getProperty("user.name").trimSpaces(),
        "project" to (result.gradle?.rootProject?.name ?: "").trimSpaces(),
        "version" to (result.gradle?.gradleVersion ?: ""),
        "os" to "${OperatingSystem.current().name}-${OperatingSystem.current().version}".trimSpaces()
    )
}

fun String.trimSpaces() = this.replace("\\s".toRegex(), "")
