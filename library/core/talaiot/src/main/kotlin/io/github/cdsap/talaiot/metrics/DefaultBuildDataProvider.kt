package io.github.cdsap.talaiot.metrics

import io.github.cdsap.talaiot.entities.ExecutionReport
import io.github.cdsap.talaiot.metrics.BuildMetrics.*

class DefaultBuildMetricsProvider(
    private val report: ExecutionReport
) : ValuesProvider {

    override fun get(): Map<String, Any> {
        val map = mutableMapOf<String, Any>()

        map[Duration.toKey()] = report.durationMs?.toLong() ?: 0L
        map[Configuration.toKey()] = report.configurationDurationMs?.toLong() ?: 0L

        with(report) {
            success.let { map[Success.toKey()] = it }
            buildId?.let { map[BuildId.toKey()] = it }
            buildInvocationId?.let { map[BuildInvocationId.toKey()] = it }
            requestedTasks?.let { map[RequestedTasks.toKey()] = it }
            cacheRatio?.let { map[CacheRatio.toKey()] = it.toDouble() }
            beginMs?.let { map[Start.toKey()] = it.toDouble() }
            scanLink?.let { map[GradleScanLink.toKey()] = it }
            rootProject?.let { map[RootProject.toKey()] = it }
            with(environment) {
                osVersion?.let { map[OsVersion.toKey()] = it }
                maxWorkers?.let { map[MaxWorkers.toKey()] = it.toInt() }
                javaRuntime?.let { map[JavaRuntime.toKey()] = it }
                javaVmName?.let { map[JavaVmName.toKey()] = it }
                javaXmsBytes?.let { map[JavaXmsBytes.toKey()] = it.toLong() }
                javaXmxBytes?.let { map[JavaXmxBytes.toKey()] = it.toLong() }
                javaMaxPermSize?.let { map[JavaMaxPermSize.toKey()] = it.toLong() }
                totalRamAvailableBytes?.let { map[TotalRamAvailableBytes.toKey()] = it.toLong() }
                cpuCount?.let { map[CpuCount.toKey()] = it.toInt() }
                locale?.let { map[Locale.toKey()] = it }
                username?.let { map[Username.toKey()] = it }
                defaultChartset?.let { map[DefaultCharset.toKey()] = it }
                ideVersion?.let { map[IdeVersion.toKey()] = it }
                gradleVersion?.let { map[GradleVersion.toKey()] = it }
                gitBranch?.let { map[GitBranch.toKey()] = it }
                gitUser?.let { map[GitUser.toKey()] = it }
                hostname?.let { map[Hostname.toKey()] = it }
                osManufacturer?.let { map[OsManufacturer.toKey()] = it }
                publicIp?.let { map[PublicIp.toKey()] = it }
                cacheUrl?.let { map[CacheUrl.toKey()] = it }
                localCacheHit?.let { map[LocalCacheHit.toKey()] = it.toString() }
                localCacheMiss?.let { map[LocalCacheMiss.toKey()] = it.toString() }
                remoteCacheHit?.let { map[RemoteCacheHit.toKey()] = it.toString() }
                remoteCacheMiss?.let { map[RemoteCacheMiss.toKey()] = it.toString() }
                cacheStore?.let { map[CacheStore.toKey()] = it }
                switches.buildCache?.let { map[SwitchCache.toKey()] = it }
                switches.buildScan?.let { map[SwitchScan.toKey()] = it }
                switches.configurationOnDemand?.let { map[SwitchConfigurationOnDemand.toKey()] = it }
                switches.continueOnFailure?.let { map[SwitchContinueOnFailure.toKey()] = it }
                switches.daemon?.let { map[SwitchDaemon.toKey()] = it }
                switches.dryRun?.let { map[SwitchDryRun.toKey()] = it }
                switches.offline?.let { map[SwitchOffline.toKey()] = it }
                switches.parallel?.let { map[SwitchParallel.toKey()] = it }
                switches.refreshDependencies?.let { map[SwitchRefreshDependencies.toKey()] = it }
                switches.rerunTasks?.let { map[SwitchRerunTasks.toKey()] = it }
            }
        }
        map.putAll(report.customProperties.buildProperties)
        return map.filter { (_, v) -> v != "undefined" }
    }
}
