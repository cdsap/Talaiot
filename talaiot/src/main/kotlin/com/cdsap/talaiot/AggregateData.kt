package com.cdsap.talaiot

import com.cdsap.talaiot.entities.TaskLength
import com.cdsap.talaiot.entities.TaskMeasurementAggregated
import org.gradle.BuildResult
import org.gradle.internal.os.OperatingSystem
import java.io.BufferedReader
import java.io.InputStreamReader

class AggregateData(val result: BuildResult, val timing: MutableList<TaskLength>) {

    fun build(): TaskMeasurementAggregated {
        val runtime = Runtime.getRuntime()
        val process = BufferedReader(
                InputStreamReader(runtime.exec("git rev-parse --abbrev-ref HEAD").inputStream))
        val totalMemory = runtime.totalMemory()
        val freeMemory = runtime.freeMemory()
        val maxMemory = runtime.maxMemory()
        val availableProcessors = runtime.availableProcessors()
        val user = System.getProperty("user.name").trimSpaces()
        val project = (result.gradle?.rootProject?.name ?: "").trimSpaces()
        val branch = process.readLine().trimSpaces()
        val version = result.gradle?.gradleVersion ?: ""
        val os = "${OperatingSystem.current().name}-${OperatingSystem.current().version}".trimSpaces()

        return TaskMeasurementAggregated(user = user,
                totalMemory = totalMemory,
                maxMemory = maxMemory,
                freeMemory = freeMemory,
                availableProcessors = availableProcessors,
                branch = branch,
                gradleVersion = version,
                os = os,
                project = project,
                taskMeasurement = timing)
    }
}

fun String.trimSpaces() = this.replace("\\s".toRegex(), "")