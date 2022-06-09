package io.github.cdsap.talaiot.report

import io.github.cdsap.talaiot.entities.CustomProperties
import io.github.cdsap.talaiot.entities.Environment
import io.github.cdsap.talaiot.entities.ExecutionReport
import io.github.cdsap.talaiot.entities.Switches
import io.github.cdsap.talaiot.entities.TaskLength
import io.github.cdsap.talaiot.entities.TaskMessageState

object ExecutionReportProvider {

    fun simpleExecutionReport(): ExecutionReport {
        return ExecutionReport(
            success = true, configurationDurationMs = "20", durationMs = "10"
        )
    }

    fun executionReport(): ExecutionReport {
        return ExecutionReport(
            requestedTasks = "assemble", durationMs = "10", success = true,
            environment = Environment(
                cpuCount = "12", maxWorkers = "4"
            ),
            customProperties = CustomProperties(
                taskProperties = getMetricsTasks(), buildProperties = getMetricsBuild()
            ),

            tasks = listOf(
                TaskLength(
                    1, "assemble", ":assemble", TaskMessageState.EXECUTED, false, "app"
                )
            )
        )
    }

    fun completeExecutionReport() = ExecutionReport(
        beginMs = "1.590661991331E12",
        endMs = "1243",
        durationMs = "10",
        buildId = "12",
        rootProject = "app",
        requestedTasks = "app:assembleDebug",
        buildInvocationId = "123",
        configurationDurationMs = "32",
        environment = Environment(
            cpuCount = "4",
            osVersion = "Linux 1.4",
            maxWorkers = "2",
            javaRuntime = "1.2",
            locale = "EN-us",
            username = "user",
            defaultChartset = "default",
            ideVersion = "2.1",
            gradleVersion = "6.2.2",
            cacheUrl = "cacheUrl",
            cacheStore = "10",
            gitBranch = "git_branch",
            gitUser = "git_user",
            switches = Switches(
                daemon = "true", offline = "true"
            ),
            hostname = "localMachine"
        ),
        success = true,
        customProperties = CustomProperties(
            taskProperties = getMetricsTasks(), buildProperties = getMetricsBuild()
        ),
        tasks = listOf(
            TaskLength(
                1, "clean", ":clean", TaskMessageState.EXECUTED, false, "app"
            )
        )
    )

    fun getMetricsTasks(): MutableMap<String, String> {
        return mutableMapOf(
            "metric1" to "value1", "metric2" to "value2"
        )
    }

    fun getMetricsBuild(): MutableMap<String, String> {
        return mutableMapOf(
            "metric3" to "value3", "metric4" to "value4"
        )
    }
}
