package com.cdsap.talaiot.metrics

import com.cdsap.talaiot.entities.ExecutionReport

class DefaultBuildMetricsProvider(
    private val report: ExecutionReport
) : ValuesProvider {

    override fun get(): Map<String, Any> {
        val map = mutableMapOf<String, Any>()

        map["duration"] = report.durationMs?.toLong() ?: 0L
        map["configuration"] = report.configurationDurationMs?.toLong() ?: 0L

        with(report) {
            success.let { map["success"] = it }
            buildId?.let { map["buildId"] = it }
            buildInvocationId?.let { map["buildInvocationId"] = it }
            requestedTasks?.let { map["requestedTasks"] = it }
            cacheRatio?.let { map["cacheRatio"] = it.toDouble() }
            beginMs?.let { map["start"] = it.toDouble() }
            rootProject?.let { map["rootProject"] = it }
            scanLink?.let { map["scanLink"] = it }

            with(environment) {
                osVersion?.let { map["osVersion"] = it }
                maxWorkers?.let { map["maxWorkers"] = it.toInt() }
                javaRuntime?.let { map["javaRuntime"] = it }
                javaVmName?.let { map["javaVmName"] = it }
                javaXmsBytes?.let { map["javaXmsBytes"] = it.toLong() }
                javaXmxBytes?.let { map["javaXmxBytes"] = it.toLong() }
                javaMaxPermSize?.let { map["javaMaxPermSize"] = it.toLong() }
                totalRamAvailableBytes?.let { map["totalRamAvailableBytes"] = it.toLong() }
                cpuCount?.let { map["cpuCount"] = it.toInt() }
                locale?.let { map["locale"] = it }
                username?.let { map["username"] = it }
                publicIp?.let { map["publicIp"] = it }
                defaultChartset?.let { map["defaultCharset"] = it }
                ideVersion?.let { map["ideVersion"] = it }
                gradleVersion?.let { map["gradleVersion"] = it }
                gitBranch?.let { map["gitBranch"] = it }
                gitUser?.let { map["gitUser"] = it }
                hostname?.let { map["hostname"] = it }
                osManufacturer?.let { map["osManufacturer"] = it }
                publicIp?.let { map["publicIp"] = it }
                cacheMode?.let { map["cacheMode"] = it }
                cachePushEnabled?.let { map["cachePushEnabled"] = it }
                cacheUrl?.let { map["cacheUrl"] = it }
                localCacheHit?.let { map["localCacheHit"] = it.toString() }
                localCacheMiss?.let { map["localCacheMiss"] = it.toString() }
                remoteCacheHit?.let { map["remoteCacheHit"] = it.toString() }
                remoteCacheMiss?.let { map["remoteCacheMiss"] = it.toString() }
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
        }
        map.putAll(report.customProperties.buildProperties)
        return map.filter { (_, v) -> v != "undefined" }
    }
}
