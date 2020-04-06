package com.cdsap.talaiot.configuration

import com.cdsap.talaiot.metrics.*
import com.cdsap.talaiot.metrics.base.Metric

/**
 * Configuration for the Metrics extensions
 *
 * metrics{
 *   default()
 *   git()
 *   performance()
 *   gradleSwitches()
 * }
 *
 * By default [default] is called unless user has specified anything at all in this configuration.
 *
 * Default includes:
 *  [RootProjectNameMetric]
 *  [GradleRequestedTasksMetric]
 *  [GradleVersionMetric]
 *  [GradleBuildCacheModeMetric]
 *  [GradleBuildCachePushEnabled]
 *  [GradleScanLinkMetric]
 *
 * Git includes:
 *  [GitUserMetric]
 *  [GitBranchMetric]
 *
 * Performance includes:
 *  [UserMetric]
 *  [OsMetric]
 *  [ProcessorCountMetric]
 *  [RamAvailableMetric]
 *  [JavaVmNameMetric]
 *  [LocaleMetric]
 *  [GradleMaxWorkersMetric]
 *  [JvmXmsMetric]
 *  [JvmXmxMetric]
 *  [JvmMaxPermSizeMetric]
 *
 * Gradle switches includes:
 *  [GradleSwitchCachingMetric]
 *  [GradleSwitchBuildScanMetric]
 *  [GradleSwitchParallelMetric]
 *  [GradleSwitchConfigureOnDemandMetric]
 *  [GradleSwitchDryRunMetric]
 *  [GradleSwitchRefreshDependenciesMetric]
 *  [GradleSwitchRerunTasksMetric]
 *  [GradleSwitchDaemonMetric]
 *
 *  If you want to define a custom metrics:
 *
 *  metrics {
 *    customBuildMetrics["key"] = "value"
 *    customTaskMetrics["key"] = "value"
 *  }
 */
class MetricsConfiguration {
    /**
     * Flag to specify the generation of the unique build id.
     * In some cases could generate high cardinality problems like in basic InfluxDb setups, disabled by default
     */
    var generateBuildId = false

    private var metrics: MutableList<Metric<*, *>> = mutableListOf()

    fun default() = metrics.run {
        add(RootProjectNameMetric())
        add(GradleRequestedTasksMetric())
        add(GradleVersionMetric())
        add(GradleBuildCacheModeMetric())
        add(GradleBuildCachePushEnabled())
        add(GradleScanLinkMetric())

        this@MetricsConfiguration
    }

    fun git() = metrics.run {
        add(GitUserMetric())
        add(GitBranchMetric())
        this@MetricsConfiguration
    }

    fun environment() = metrics.run {
        add(OsManufacturerMetric())
        add(HostnameMetric())
        add(PublicIpMetric())
        add(DefaultCharsetMetric())
        this@MetricsConfiguration
    }

    fun performance() = metrics.run {
        add(UserMetric())
        add(OsMetric())
        add(ProcessorCountMetric())
        add(RamAvailableMetric())
        add(JavaVmNameMetric())
        add(LocaleMetric())
        add(GradleMaxWorkersMetric())
        add(JvmXmsMetric())
        add(JvmXmxMetric())
        add(JvmMaxPermSizeMetric())
        this@MetricsConfiguration
    }

    fun gradleSwitches() = metrics.run {
        add(GradleSwitchCachingMetric())
        add(GradleSwitchBuildScanMetric())
        add(GradleSwitchParallelMetric())
        add(GradleSwitchConfigureOnDemandMetric())
        add(GradleSwitchDryRunMetric())
        add(GradleSwitchRefreshDependenciesMetric())
        add(GradleSwitchRerunTasksMetric())
        add(GradleSwitchDaemonMetric())
        this@MetricsConfiguration
    }

    /**
     * Adds the given custom metrics into the metrics list.
     *
     * @param customMetrics takes N [Metric]s to be added to the metrics list.
     */
    fun customMetrics(vararg customMetrics: Metric<*, *>) {
        customMetrics.forEach {
            metrics.add(it)
        }
    }

    fun customBuildMetrics(vararg buildMetrics: Pair<String, String>) {
        buildMetrics.mapTo(metrics) {
            createSimpleBuildMetric(it)
        }
    }
    fun customBuildMetrics(buildMetric: Pair<String, String>) {
        metrics.add(createSimpleBuildMetric(buildMetric))
    }
    fun customBuildMetrics(buildMetrics: Map<String, String>) {
        buildMetrics.mapTo(metrics) {
            createSimpleBuildMetric(it.toPair())
        }
    }

    fun customTaskMetrics(vararg taskMetrics: Pair<String, String>) {
        taskMetrics.mapTo(metrics) {
            createSimpleTaskMetric(it)
        }
    }
    fun customTaskMetrics(taskMetric: Pair<String, String>) {
        metrics.add(createSimpleTaskMetric(taskMetric))
    }
    fun customTaskMetrics(taskMetrics: Map<String, String>) {
        taskMetrics.mapTo(metrics) {
            createSimpleTaskMetric(it.toPair())
        }
    }

    internal fun build(): MutableList<Metric<*, *>> {
        // If there was any customization then we assume that user populated everything manually, otherwise defaults
        if (metrics.isEmpty()) {
            default()
            performance()
            gradleSwitches()
            git()
            environment()
        }

        if (generateBuildId) {
            metrics.add(BuildIdMetric())
        }

        return metrics
    }

    private fun createSimpleBuildMetric(pair: Pair<String, String>): SimpleMetric<String> {
        return SimpleMetric(
            provider = { pair.second },
            assigner = { report, value -> report.customProperties.buildProperties[pair.second] = value }
        )
    }

    private fun createSimpleTaskMetric(pair: Pair<String, String>): SimpleMetric<String> {
        return SimpleMetric(
            provider = { pair.second },
            assigner = { report, value -> report.customProperties.taskProperties[pair.second] = value }
        )
    }
}