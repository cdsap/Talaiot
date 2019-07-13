package com.cdsap.talaiot.entities

data class ExecutionReport(
    var environment: Environment = Environment(),
    var customProperties: CustomProperties = CustomProperties(),
    var beginMs: String? = null,
    var endMs: String? = null,
    var durationMs: String? = null,
    var configurationDurationMs: String? = null,
    var tasks: List<TaskLength>? = null,
    var unfilteredTasks: List<TaskLength>? = null,
    var buildId: String? = null,
    var rootProject: String? = null,
    var requestedTasks: String? = null,
    var success: Boolean = false,
    var scanLink: String? = null,
    var buildInvocationId: String? = null
) {

    val cacheRatio: String?
        get() = tasks?.let {
            it.count { taskLength -> taskLength.state == TaskMessageState.FROM_CACHE } / it.size.toDouble()
        }?.toString()

    fun flattenBuildEnv(): Map<String, String> {
        val map = mutableMapOf<String, String>()

        with(environment) {
            cacheMode?.let { map["cacheMode"] = it }
            cachePushEnabled?.let { map["cachePushEnabled"] = it }
            cacheUrl?.let { map["cacheUrl"] = it }
            cacheHit?.let { map["cacheHit"] = it }
            cacheMiss?.let { map["cacheMiss"] = it }
            cacheStore?.let { map["cacheStore"] = it }

            switches.buildCache?.let { map["switch.cache"] = it }
            switches.buildScan?.let { map["switch.scan"] = it }
            switches.configurationOnDemand?.let { map["switch.configurationOnDemand"] = it }
            switches.continueOnFailure?.let { map["switch.continueOnFailure"] = it }
            switches.daemon?.let { map["switch.daemon"] = it }
            switches.dryRun?.let { map["switch.dryRun"] = it }
            switches.offline?.let { map["switch.offline"] = it }
            switches.parallel?.let { map["switch.parallel"] = it }
            switches.refreshDependencies?.let { map["switch.refreshDependencies"] = it }
            switches.rerunTasks?.let { map["switch.rerunTasks"] = it }
        }

        environment.osVersion?.let { map["osVersion"] = it }
        environment.javaVmName?.let { map["javaVmName"] = it }
        environment.cpuCount?.let { map["cpuCount"] = it }
        environment.username?.let { map["username"] = it }
        environment.gradleVersion?.let { map["gradleVersion"] = it }

        buildId?.let { map["buildId"] = it }
        rootProject?.let { map["rootProject"] = it }
        requestedTasks?.let { map["requestedTasks"] = it }

        //These come last to have an ability to override calculation
        map.putAll(customProperties.properties)

        return map.filter { (k, v) -> v != "undefined" }
    }

    /**
     * This would be a lot faster if it was actually a weighted graph
     */
    fun estimateCriticalPath() {
        var currentRoot: TaskLength? = tasks?.find { it.rootNode } ?: return

        while(currentRoot != null) {
            currentRoot.critical = true

            val dependencies = currentRoot.taskDependencies
                .mapNotNull { dep ->
                    tasks?.find { it.taskPath == dep }
                }

            currentRoot = dependencies.maxBy { it.stopMs } ?: null
        }
    }
}

data class Environment(
    var cpuCount: String? = null,
    var osVersion: String? = null,
    var maxWorkers: String? = null,
    var javaRuntime: String? = null,
    var javaVmName: String? = null,
    var javaXmsBytes: String? = null,
    var javaXmxBytes: String? = null,
    var javaMaxPermSize: String? = null,
    var totalRamAvailableBytes: String? = null,
    var locale: String? = null,
    var username: String? = null,
    var publicIp: String? = null,
    var defaultChartset: String? = null,
    var ideVersion: String? = null,
    var gradleVersion: String? = null,
    var cacheMode: String? = null,
    var cachePushEnabled: String? = null,
    var cacheUrl: String? = null,
    var cacheHit: String? = null,
    var cacheMiss: String? = null,
    var cacheStore: String? = null,
    var plugins: List<Plugin> = emptyList(),
    var gitBranch: String? = null,
    var gitUser: String? = null,
    var switches: Switches = Switches(),
    var hostname: String? = null,
    var osManufacturer: String? = null
)

data class Switches(
    var buildCache: String? = null,
    var configurationOnDemand: String? = null,
    var daemon: String? = null,
    var parallel: String? = null,
    var continueOnFailure: String? = null,
    var dryRun: String? = null,
    var offline: String? = null,
    var rerunTasks: String? = null,
    var refreshDependencies: String? = null,
    var buildScan: String? = null
)

data class CustomProperties(
    var properties: MutableMap<String, String> = mutableMapOf()
)

data class Plugin(
    var id: String,
    var mainClass: String,
    var version: String
)