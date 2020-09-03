package com.cdsap.talaiot.report

import com.cdsap.talaiot.entities.*

object ExecutionReportProvider {

    fun simpleExecutionReport(): ExecutionReport {
        return ExecutionReport(
            success = true,
            configurationDurationMs = "20",
            durationMs = "10"
        )
    }

    fun executionReport(): ExecutionReport {
        return ExecutionReport(
            requestedTasks = "assemble",
            durationMs = "10",
            success = true,
            environment = Environment(
                cpuCount = "12", maxWorkers = "4"
            ),
            customProperties = CustomProperties(
                taskProperties = getMetricsTasks(),
                buildProperties = getMetricsBuild()
            ),

            tasks = listOf(
                TaskLength(
                    1, "assemble", ":assemble", TaskMessageState.EXECUTED, false,
                    "app", emptyList()
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
        scanLink = "www.scan.link",
        buildInvocationId = "123",
        configurationDurationMs = "32",
        environment = Environment(
            cpuCount = "4",
            osVersion = "Linux 1.4",
            maxWorkers = "2",
            javaRuntime = "1.2",
            locale = "EN-us",
            username = "user",
            publicIp = "127.0.0.1",
            defaultChartset = "default",
            ideVersion = "2.1",
            gradleVersion = "6.2.2",
            cacheMode = "cacheMode",
            cachePushEnabled = "true",
            cacheUrl = "cacheUrl",
            cacheStore = "10",
            localCacheHit = 1,
            localCacheMiss = 0,
            remoteCacheHit = 0,
            remoteCacheMiss = 0,
            gitBranch = "git_branch",
            gitUser = "git_user",
            switches = Switches(
                daemon = "true",
                offline = "true"
            ),
            hostname = "localMachine",
            osManufacturer = "osManufact4r"
        ),
        success = true,
        customProperties = CustomProperties(
            taskProperties = getMetricsTasks(),
            buildProperties = getMetricsBuild()
        ),
        tasks = listOf(
            TaskLength(
                1, "clean", ":clean", TaskMessageState.EXECUTED, false,
                "app", emptyList()
            )
        )
    )

     fun getMetricsTasks(): MutableMap<String, String> {
        return mutableMapOf(
            "metric1" to "value1",
            "metric2" to "value2"
        )
    }

     fun getMetricsBuild(): MutableMap<String, String> {
        return mutableMapOf(
            "metric3" to "value3",
            "metric4" to "value4"
        )
    }
}
