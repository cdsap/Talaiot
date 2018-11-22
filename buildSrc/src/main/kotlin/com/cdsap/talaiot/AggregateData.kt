package com.cdsap.talaiot

import com.agoda.gradle.tracking.entities.TaskLenght
import com.agoda.gradle.tracking.entities.TaskMeasurementAggregated
import org.gradle.BuildResult
import org.gradle.internal.os.OperatingSystem
import java.io.BufferedReader
import java.io.InputStreamReader

class AggregateData(val result: BuildResult, val timing: MutableList<TaskLenght>) {

    fun build(): TaskMeasurementAggregated {
        val runtime = Runtime.getRuntime()
        val process = BufferedReader(
                InputStreamReader(runtime.exec("git rev-parse --abbrev-ref HEAD").inputStream))
        val totalMemory = runtime.totalMemory()
        val freeMemory = runtime.freeMemory()
        val maxMemory = runtime.maxMemory()
        val availableProcessors = runtime.availableProcessors()
        val user = System.getProperty("user.name")
        val project = result.gradle?.rootProject?.name ?: ""
        val branch = process.readLine()
        val version = result.gradle?.gradleVersion ?: ""
        val os = "${OperatingSystem.current().name}-${OperatingSystem.current().version}"

        return TaskMeasurementAggregated(user = user,
                totalMemory = totalMemory,
                maxMemory = maxMemory,
                freeMemory = freeMemory,
                availableProcessors = availableProcessors,
                branch = branch,
                gradleVersion = version,
                os = os,
                project = project,
                taskMeasurment = timing)
    }
}
