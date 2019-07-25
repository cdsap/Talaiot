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

    var metrics: MutableList<Metric<*, *>> = mutableListOf()

    var customBuildMetrics: MutableMap<String, String> = mutableMapOf()
    var customTaskMetrics: MutableMap<String, String> = mutableMapOf()

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

    fun gradleSwithes() = metrics.run {
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

    fun customBuildMetrics(vararg pair: Pair<String, String>) {
        pair.forEach {
            customBuildMetrics[it.first] = it.second
        }
    }
    fun customBuildMetrics(pair: Pair<String, String>) {
        customBuildMetrics[pair.first] = pair.second
    }
    fun customBuildMetrics(metrics: Map<String, String>) {
        metrics.forEach {
            customBuildMetrics[it.key] = it.value
        }
    }

    fun customTaskMetrics(vararg pair: Pair<String, String>) {
        pair.forEach {
            customTaskMetrics[it.first] = it.second
        }
    }
    fun customTaskMetrics(pair: Pair<String, String>) {
        customTaskMetrics[pair.first] = pair.second
    }
    fun customTaskMetrics(metrics: Map<String, String>) {
        metrics.forEach {
            customTaskMetrics[it.key] = it.value
        }
    }

    internal fun build(): MutableList<Metric<*, *>> {
        // If there was any customization then we assume that user populated everything manually, otherwise defaults
        if (metrics.isEmpty()) {
            default()
            performance()
            gradleSwithes()
        }

        if (generateBuildId) {
            metrics.add(BuildIdMetric())
        }

        addCustomBuildMetrics()

        return metrics
    }

    private fun addCustomBuildMetrics() {
        customBuildMetrics.forEach { metric ->
            metrics.add(
                SimpleMetric(
                    provider = { metric.value },
                    assigner = { report, value -> report.customProperties.buildProperties[metric.key] = value }
                )
            )
        }
    }

    private fun addCustomTaskMetrics() {
        customTaskMetrics.forEach { metric ->
            metrics.add(
                SimpleMetric(
                    provider = { metric.value },
                    assigner = { report, value -> report.customProperties.taskProperties[metric.key] = value }
                )
            )
        }
    }
}