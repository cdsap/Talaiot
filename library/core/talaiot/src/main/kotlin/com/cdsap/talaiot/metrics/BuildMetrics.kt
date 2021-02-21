package com.cdsap.talaiot.metrics

enum class BuildMetrics {
    Duration,
    Configuration,
    Success,
    BuildId,
    BuildInvocationId,
    RequestedTasks,
    CacheRatio,
    Start,
    RootProject,
    OsVersion,
    MaxWorkers,
    JavaRuntime,
    JavaVmName,
    JavaXmsBytes,
    JavaXmxBytes,
    JavaMaxPermSize,
    TotalRamAvailableBytes,
    CpuCount,
    Locale,
    Username,
    DefaultCharset,
    IdeVersion,
    GradleVersion,
    GitBranch,
    GitUser,
    Hostname,
    OsManufacturer,
    PublicIp,
    CacheUrl,
    LocalCacheHit,
    LocalCacheMiss,
    RemoteCacheHit,
    RemoteCacheMiss,
    CacheStore,
    SwitchCache,
    SwitchCscan,
    SwitchConfigurationOnDemand,
    SwitchContinueOnFailure,
    SwitchDaemon,
    SwitchDryRun,
    SwitchOffline,
    SwitchParallel,
    SwitchRefreshDependencies,
    SwitchRerunTasks,
    Custom;

    override fun toString(): String {
        return if (super.toString().startsWith("Switch")) {
            val temp = super.toString().split("Switch")
            "switch.${temp[0].decapitalize()}"
        } else {
            super.toString().decapitalize()
        }
    }
}
