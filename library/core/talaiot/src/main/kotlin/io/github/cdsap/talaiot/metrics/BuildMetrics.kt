package io.github.cdsap.talaiot.metrics

enum class BuildMetrics : Metrics {
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
    CpuCount,
    Locale,
    Username,
    DefaultCharset,
    IdeVersion,
    GradleVersion,
    GradleScanLink,
    GitBranch,
    GitUser,
    Hostname,
    CacheUrl,
    CacheStore,
    SwitchCache,
    SwitchScan,
    SwitchConfigurationOnDemand,
    SwitchContinueOnFailure,
    SwitchDaemon,
    SwitchDryRun,
    SwitchOffline,
    SwitchParallel,
    SwitchRefreshDependencies,
    SwitchRerunTasks,
    Custom {
        override val isCustom: Boolean = true
    };

    override fun toKey(): String = toString()

    override fun toString(): String {
        return if (super.toString().startsWith("Switch")) {
            val temp = super.toString().split("Switch")
            "switch.${temp[1].decapitalize()}"
        } else {
            super.toString().decapitalize()
        }
    }
}
