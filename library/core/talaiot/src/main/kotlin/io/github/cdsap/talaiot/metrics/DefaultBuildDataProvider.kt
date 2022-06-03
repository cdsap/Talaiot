package io.github.cdsap.talaiot.metrics

import io.github.cdsap.talaiot.entities.ExecutionReport

class DefaultBuildMetricsProvider(
    private val report: ExecutionReport
) : ValuesProvider, java.io.Serializable {

    override fun get(): Map<String, Any> {
        val map = mutableMapOf<String, Any>()

        map[BuildMetrics.Duration.toKey()] = report.durationMs?.toLong() ?: 0L
        map[BuildMetrics.Configuration.toKey()] = report.configurationDurationMs?.toLong() ?: 0L

        with(report) {
            success.let { map[BuildMetrics.Success.toKey()] = it }
            buildId?.let { map[BuildMetrics.BuildId.toKey()] = it }
            buildInvocationId?.let { map[BuildMetrics.BuildInvocationId.toKey()] = it }
            requestedTasks?.let { map[BuildMetrics.RequestedTasks.toKey()] = it }
            cacheRatio?.let { map[BuildMetrics.CacheRatio.toKey()] = it.toDouble() }
            beginMs?.let { map[BuildMetrics.Start.toKey()] = it.toDouble() }
            scanLink?.let { map[BuildMetrics.GradleScanLink.toKey()] = it }
            rootProject?.let { map[BuildMetrics.RootProject.toKey()] = it }
            with(environment) {
                osVersion?.let { map[BuildMetrics.OsVersion.toKey()] = it }
                maxWorkers?.let { map[BuildMetrics.MaxWorkers.toKey()] = it.toInt() }
                javaRuntime?.let { map[BuildMetrics.JavaRuntime.toKey()] = it }
                javaVmName?.let { map[BuildMetrics.JavaVmName.toKey()] = it }
                javaXmsBytes?.let { map[BuildMetrics.JavaXmsBytes.toKey()] = it.toLong() }
                javaXmxBytes?.let { map[BuildMetrics.JavaXmxBytes.toKey()] = it.toLong() }
                javaMaxPermSize?.let { map[BuildMetrics.JavaMaxPermSize.toKey()] = it.toLong() }
                cpuCount?.let { map[BuildMetrics.CpuCount.toKey()] = it.toInt() }
                locale?.let { map[BuildMetrics.Locale.toKey()] = it }
                username?.let { map[BuildMetrics.Username.toKey()] = it }
                defaultChartset?.let { map[BuildMetrics.DefaultCharset.toKey()] = it }
                ideVersion?.let { map[BuildMetrics.IdeVersion.toKey()] = it }
                gradleVersion?.let { map[BuildMetrics.GradleVersion.toKey()] = it }
                gitBranch?.let { map[BuildMetrics.GitBranch.toKey()] = it }
                gitUser?.let { map[BuildMetrics.GitUser.toKey()] = it }
                hostname?.let { map[BuildMetrics.Hostname.toKey()] = it }
                osManufacturer?.let { map[BuildMetrics.OsManufacturer.toKey()] = it }
                publicIp?.let { map[BuildMetrics.PublicIp.toKey()] = it }
                cacheUrl?.let { map[BuildMetrics.CacheUrl.toKey()] = it }
                localCacheHit?.let { map[BuildMetrics.LocalCacheHit.toKey()] = it.toString() }
                localCacheMiss?.let { map[BuildMetrics.LocalCacheMiss.toKey()] = it.toString() }
                remoteCacheHit?.let { map[BuildMetrics.RemoteCacheHit.toKey()] = it.toString() }
                remoteCacheMiss?.let { map[BuildMetrics.RemoteCacheMiss.toKey()] = it.toString() }
                cacheStore?.let { map[BuildMetrics.CacheStore.toKey()] = it }
                switches.buildCache?.let { map[BuildMetrics.SwitchCache.toKey()] = it }
                switches.buildScan?.let { map[BuildMetrics.SwitchScan.toKey()] = it }
                switches.configurationOnDemand?.let { map[BuildMetrics.SwitchConfigurationOnDemand.toKey()] = it }
                switches.continueOnFailure?.let { map[BuildMetrics.SwitchContinueOnFailure.toKey()] = it }
                switches.daemon?.let { map[BuildMetrics.SwitchDaemon.toKey()] = it }
                switches.dryRun?.let { map[BuildMetrics.SwitchDryRun.toKey()] = it }
                switches.offline?.let { map[BuildMetrics.SwitchOffline.toKey()] = it }
                switches.parallel?.let { map[BuildMetrics.SwitchParallel.toKey()] = it }
                switches.refreshDependencies?.let { map[BuildMetrics.SwitchRefreshDependencies.toKey()] = it }
                switches.rerunTasks?.let { map[BuildMetrics.SwitchRerunTasks.toKey()] = it }
            }
        }
        map.putAll(report.customProperties.buildProperties)
        return map.filter { (_, v) -> v != "undefined" }
    }
}
