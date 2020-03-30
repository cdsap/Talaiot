package com.cdsap.talaiot.metrics

import com.cdsap.talaiot.entities.ExecutionReport

class DefaultBuildMetricsProvider(
    private val report: ExecutionReport
) : ValuesProvider {

    override fun get(): Map<String, Any> {
        val metrics = mutableMapOf<String, Any>()
        metrics["duration"] = report.durationMs?.toLong() ?: 0L
        metrics["configuration"] = report.configurationDurationMs?.toLong() ?: 0L
        metrics["success"] = report.success
        report.environment.osVersion?.let { metrics["osVersion"] = it }
        report.environment.maxWorkers?.let { metrics["maxWorkers"] = it.toInt() }
        report.environment.javaRuntime?.let { metrics["javaRuntime"] = it }
        report.environment.javaVmName?.let { metrics["javaVmName"] = it }
        report.environment.javaXmsBytes?.let { metrics["javaXmsBytes"] = it.toLong() }
        report.environment.javaXmxBytes?.let { metrics["javaXmxBytes"] = it.toLong() }
        report.environment.javaMaxPermSize?.let { metrics["javaMaxPermSize"] = it.toLong() }
        report.environment.totalRamAvailableBytes?.let { metrics["totalRamAvailableBytes"] = it.toLong() }
        report.environment.cpuCount?.let { metrics["cpuCount"] = it.toInt() }
        report.environment.locale?.let { metrics["locale"] = it }
        report.environment.username?.let { metrics["username"] = it }
        report.environment.publicIp?.let { metrics["publicIp"] = it }
        report.environment.defaultChartset?.let { metrics["defaultCharset"] = it }
        report.environment.ideVersion?.let { metrics["ideVersion"] = it }
        report.environment.gradleVersion?.let { metrics["gradleVersion"] = it }
        report.environment.gitBranch?.let { metrics["gitBranch"] = it }
        report.environment.gitUser?.let { metrics["gitUser"] = it }
        report.environment.hostname?.let { metrics["hostname"] = it }
        report.environment.osManufacturer?.let { metrics["osManufacturer"] = it }
        report.environment.publicIp?.let { metrics["publicIp"] = it }
        report.cacheRatio?.let { metrics["cacheRatio"] = it.toDouble() }
        report.beginMs?.let { metrics["start"] = it.toDouble() }
        report.rootProject?.let { metrics["rootProject"] = it }
        report.requestedTasks?.let { metrics["requestedTasks"] = it }
        report.scanLink?.let { metrics["scanLink"] = it }
        return metrics
    }
}
