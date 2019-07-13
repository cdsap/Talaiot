package com.cdsap.talaiot.configuration

import com.cdsap.talaiot.metrics.*
import com.cdsap.talaiot.metrics.base.Metric

/**
 * Configuration for the Metrics extensions
 * It will process the default metrics defined and the custom ones.
 * metrics{
 *   gitMetrics = true
 *   performanceMetrics = false
 * }
 */
class MetricsConfiguration {
    /**
     * Flag to specify the generation of the unique build id.
     * In some cases could generate high cardinality problems like in basic InfluxDb setups, disabled by default
     */
    var generateBuildId = false

    var metrics: MutableList<Metric<*, *>> = mutableListOf()

    var customMetrics: MutableMap<String, String> = mutableMapOf()

    fun default() = metrics.run {
        add(RootProjectNameMetric())
        add(GradleRequestedTasksMetric())
        add(GradleVersionMetric())
        add(GradleBuildCacheModeMetric())
        add(GradleBuildCachePushEnabled())
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

    /**
     * process the metrics defined in the configuration
     * @param pair one or more pairs of strings representing custom the metrics
     */
    fun customMetrics(vararg pair: Pair<String, String>) {
        pair.forEach {
            customMetrics[it.first] = it.second
        }
    }

    /**
     * process the metrics defined in the configuration
     * @param pair one pair of strings representing a custom metric
     */
    fun customMetrics(pair: Pair<String, String>) {
        customMetrics[pair.first] = pair.second
    }

    /**
     * process the metrics defined in the configuration
     * @param metrics a Map of strings representing  custom metrics
     */
    fun customMetrics(metrics: Map<String, String>) {
        metrics.forEach {
            customMetrics[it.key] = it.value
        }
    }

    internal fun build(): MutableList<Metric<*, *>> {
        // If there was any customization then we assume that user populated everything manually, otherwise defaults
        if (metrics.isEmpty()) {
            default()
        }

        if(generateBuildId) {
            metrics.add(BuildIdMetric())
        }

        addCustomMetrics()

        return metrics
    }

    private fun addCustomMetrics() {
        customMetrics.forEach { metric ->
            metrics.add(
                SimpleMetric(
                    provider = { metric.value },
                    assigner = { report, value -> report.customProperties.properties[metric.key] = value }
                )
            )
        }
    }
}