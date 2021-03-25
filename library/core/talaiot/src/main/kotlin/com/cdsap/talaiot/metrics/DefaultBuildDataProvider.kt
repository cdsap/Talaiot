package com.cdsap.talaiot.metrics

import com.cdsap.talaiot.entities.ExecutionReport
import com.cdsap.talaiot.metrics.BuildMetrics.*

class DefaultBuildMetricsProvider(
    private val report: ExecutionReport
) : ValuesProvider {

    override fun get(): Map<String, Any> {
        val map = mutableMapOf<String, Any>()

        map[Duration.toString()] = report.durationMs?.toLong() ?: 0L
        map[Configuration.toString()] = report.configurationDurationMs?.toLong() ?: 0L

        with(report) {
            success.let { map[Success.toString()] = it }
            buildId?.let { map[BuildId.toString()] = it }
            buildInvocationId?.let { map[BuildInvocationId.toString()] = it }
            requestedTasks?.let { map[RequestedTasks.toString()] = it }
            cacheRatio?.let { map[CacheRatio.toString()] = it.toDouble() }
            beginMs?.let { map[Start.toString()] = it.toDouble() }
            scanLink?.let { map[GradleScanLink.toString()] = it }
            rootProject?.let { map[RootProject.toString()] = it }
            with(environment) {
                osVersion?.let { map[OsVersion.toString()] = it }
                maxWorkers?.let { map[MaxWorkers.toString()] = it.toInt() }
                javaRuntime?.let { map[JavaRuntime.toString()] = it }
                javaVmName?.let { map[JavaVmName.toString()] = it }
                javaXmsBytes?.let { map[JavaXmsBytes.toString()] = it.toLong() }
                javaXmxBytes?.let { map[JavaXmxBytes.toString()] = it.toLong() }
                javaMaxPermSize?.let { map[JavaMaxPermSize.toString()] = it.toLong() }
                totalRamAvailableBytes?.let { map[TotalRamAvailableBytes.toString()] = it.toLong() }
                cpuCount?.let { map[CpuCount.toString()] = it.toInt() }
                locale?.let { map[Locale.toString()] = it }
                username?.let { map[Username.toString()] = it }
                defaultChartset?.let { map[DefaultCharset.toString()] = it }
                ideVersion?.let { map[IdeVersion.toString()] = it }
                gradleVersion?.let { map[GradleVersion.toString()] = it }
                gitBranch?.let { map[GitBranch.toString()] = it }
                gitUser?.let { map[GitUser.toString()] = it }
                hostname?.let { map[Hostname.toString()] = it }
                osManufacturer?.let { map[OsManufacturer.toString()] = it }
                publicIp?.let { map[PublicIp.toString()] = it }
                cacheUrl?.let { map[CacheUrl.toString()] = it }
                localCacheHit?.let { map[LocalCacheHit.toString()] = it.toString() }
                localCacheMiss?.let { map[LocalCacheMiss.toString()] = it.toString() }
                remoteCacheHit?.let { map[RemoteCacheHit.toString()] = it.toString() }
                remoteCacheMiss?.let { map[RemoteCacheMiss.toString()] = it.toString() }
                cacheStore?.let { map[CacheStore.toString()] = it }
                switches.buildCache?.let { map[SwitchCache.toString()] = it }
                switches.buildScan?.let { map[SwitchScan.toString()] = it }
                switches.configurationOnDemand?.let { map[SwitchConfigurationOnDemand.toString()] = it }
                switches.continueOnFailure?.let { map[SwitchContinueOnFailure.toString()] = it }
                switches.daemon?.let { map[SwitchDaemon.toString()] = it }
                switches.dryRun?.let { map[SwitchDryRun.toString()] = it }
                switches.offline?.let { map[SwitchOffline.toString()] = it }
                switches.parallel?.let { map[SwitchParallel.toString()] = it }
                switches.refreshDependencies?.let { map[SwitchRefreshDependencies.toString()] = it }
                switches.rerunTasks?.let { map[SwitchRerunTasks.toString()] = it }
            }
        }
        map.putAll(report.customProperties.buildProperties)
        return map.filter { (_, v) -> v != "undefined" }
    }
}
