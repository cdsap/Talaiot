package com.cdsap.talaiot.metrics

import com.cdsap.talaiot.trimSpaces
import org.gradle.BuildResult
import org.gradle.internal.os.OperatingSystem
import java.io.BufferedReader
import java.io.InputStreamReader

class BaseMetrics(val result: BuildResult) : Metrics {

    override fun get(): MutableMap<String, String> {
        val nas = mutableMapOf<String, String>()
        val runtime = Runtime.getRuntime()
        val process = BufferedReader(
            InputStreamReader(runtime.exec("git rev-parse --abbrev-ref HEAD").inputStream)
        )
        nas["totalMemory"] = "${runtime.totalMemory()}"
        nas["freeMemory"] = "${runtime.freeMemory()}"
        nas["maxMemory"] = "${runtime.maxMemory()}"
        nas["availableProcessors"] = "${runtime.availableProcessors()}"
        nas["user"] = System.getProperty("user.name").trimSpaces()
        nas["project"] = (result.gradle?.rootProject?.name ?: "").trimSpaces()
        nas["branch"] = process.readLine().trimSpaces()
        nas["version"] = result.gradle?.gradleVersion ?: ""
        nas["os"] = "${OperatingSystem.current().name}-${OperatingSystem.current().version}".trimSpaces()
        return nas
    }

}