package com.cdsap.talaiot.metrics

import com.cdsap.talaiot.trimSpaces
import org.gradle.BuildResult
import org.gradle.internal.os.OperatingSystem
import java.io.BufferedReader
import java.io.InputStreamReader

class BaseMetrics(private val result: BuildResult) : Metrics {

    override fun get(): MutableMap<String, String> {
        val nas = mutableMapOf<String, String>()
        nas["user"] = System.getProperty("user.name").trimSpaces()
        nas["project"] = (result.gradle?.rootProject?.name ?: "").trimSpaces()
        nas["version"] = result.gradle?.gradleVersion ?: ""
        nas["os"] = "${OperatingSystem.current().name}-${OperatingSystem.current().version}".trimSpaces()
        return nas
    }

}