package com.cdsap.talaiot.entities

data class ExecutionReport(
    var environment: Environment = Environment(),
    var customProperties: CustomProperties = CustomProperties(),
    var beginMs: String? = null,
    var endMs: String? = null,
    var durationMs: String? = null,
    var tasks: List<TaskLength>? = null,
    var unfilteredTasks: List<TaskLength>? = null,
    var buildId: String? = null,
    var rootProject: String? = null
) {
    fun flattenBuildEnv(): Map<String, String> {
        val map = mutableMapOf<String, String>()

        with(environment) {
            cpuCount?.let { map["cpuCount"] = it }
            osVersion?.let { map["osVersion"] = it }
            maxWorkers?.let { map["maxWorkers"] = it }
            javaRuntime?.let { map["javaRuntime"] = it }
            javaVmName?.let { map["javaVmName"] = it }
            javaXmsBytes?.let { map["javaXmsBytes"] = it }
            javaXmxBytes?.let { map["javaXmxBytes"] = it }
            javaMaxPermSize?.let { map["javaMaxPermSize"] = it }
            totalRamAvailableBytes?.let { map["totalRamAvailableBytes"] = it }
            locale?.let { map["locale"] = it }
            username?.let { map["username"] = it }
            publicIp?.let { map["publicIp"] = it }
            defaultChartset?.let { map["defaultCharset"] = it }
            ideVersion?.let { map["ideVersion"] = it }
            gradleVersion?.let { map["gradleVersion"] = it }
            cacheMode?.let { map["cacheMode"] = it }
            cachePushEnabled?.let { map["cachePushEnabled"] = it }
            cacheUrl?.let { map["cacheUrl"] = it }
            cacheHit?.let { map["cacheHit"] = it }
            cacheMiss?.let { map["cacheMiss"] = it }
            cacheStore?.let { map["cacheStore"] = it }
            gitBranch?.let { map["gitBranch"] = it }
            gitUser?.let { map["gitUser"] = it }

            switches?.let {
                it.buildCache?.let { map["switch.cache"] = it }
                it.buildScan?.let { map["switch.scan"] = it }
                it.configurationOnDemand?.let { map["switch.configurationOnDemand"] = it }
                it.continueOnFailure?.let { map["switch.continueOnFailure"] = it }
                it.daemon?.let { map["switch.daemon"] = it }
                it.dryRun?.let { map["switch.dryRun"] = it }
                it.offline?.let { map["switch.offline"] = it }
                it.parallel?.let { map["switch.parallel"] = it }
                it.refreshDependencies?.let { map["switch.refreshDependencies"] = it }
                it.rerunTasks?.let { map["switch.rerunTasks"] = it }
            }
        }

        durationMs?.let { map["duration"] = it }
        buildId?.let { map["buildId"] = it }
        rootProject?.let { map["rootProject"] = it }

        //These come last to have an ability to override calculation
        map.putAll(customProperties.properties)

        return map
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
    var switches: Switches = Switches()
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