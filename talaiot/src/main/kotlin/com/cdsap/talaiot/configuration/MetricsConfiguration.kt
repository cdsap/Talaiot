package com.cdsap.talaiot.configuration

import com.cdsap.talaiot.entities.ExecutionReport
import com.cdsap.talaiot.metrics.BuildIdMetric
import com.cdsap.talaiot.metrics.CacheHitMetric
import com.cdsap.talaiot.metrics.DefaultCharsetMetric
import com.cdsap.talaiot.metrics.GitBranchMetric
import com.cdsap.talaiot.metrics.GitUserMetric
import com.cdsap.talaiot.metrics.GradleBuildCacheModeMetric
import com.cdsap.talaiot.metrics.GradleBuildCachePushEnabled
import com.cdsap.talaiot.metrics.GradleMaxWorkersMetric
import com.cdsap.talaiot.metrics.GradleRequestedTasksMetric
import com.cdsap.talaiot.metrics.GradleScanLinkMetric
import com.cdsap.talaiot.metrics.GradleSwitchBuildScanMetric
import com.cdsap.talaiot.metrics.GradleSwitchCachingMetric
import com.cdsap.talaiot.metrics.GradleSwitchConfigureOnDemandMetric
import com.cdsap.talaiot.metrics.GradleSwitchDaemonMetric
import com.cdsap.talaiot.metrics.GradleSwitchDryRunMetric
import com.cdsap.talaiot.metrics.GradleSwitchParallelMetric
import com.cdsap.talaiot.metrics.GradleSwitchRefreshDependenciesMetric
import com.cdsap.talaiot.metrics.GradleSwitchRerunTasksMetric
import com.cdsap.talaiot.metrics.GradleVersionMetric
import com.cdsap.talaiot.metrics.HostnameMetric
import com.cdsap.talaiot.metrics.JavaVmNameMetric
import com.cdsap.talaiot.metrics.JvmMaxPermSizeMetric
import com.cdsap.talaiot.metrics.JvmXmsMetric
import com.cdsap.talaiot.metrics.JvmXmxMetric
import com.cdsap.talaiot.metrics.LocaleMetric
import com.cdsap.talaiot.metrics.OsManufacturerMetric
import com.cdsap.talaiot.metrics.OsMetric
import com.cdsap.talaiot.metrics.ProcessorCountMetric
import com.cdsap.talaiot.metrics.PublicIpMetric
import com.cdsap.talaiot.metrics.RamAvailableMetric
import com.cdsap.talaiot.metrics.RootProjectNameMetric
import com.cdsap.talaiot.metrics.SimpleMetric
import com.cdsap.talaiot.metrics.UserMetric
import com.cdsap.talaiot.metrics.base.Metric

/**
 * Configuration for the Metrics extensions
 *
 * ```
 * metrics {
 *   defaultMetrics = true
 *   gitMetrics = true
 *   performanceMetrics = true
 *   gradleSwitchesMetrics = true
 * }
 * ```
 *
 * By default Talaiot enables
 *  [defaultMetrics], [performanceMetrics], [gradleSwitchesMetrics], [gitMetrics] and [environmentMetrics] metrics.
 *  User can disable each of them.
 *
 * [defaultMetrics] includes:
 *  [RootProjectNameMetric]
 *  [GradleRequestedTasksMetric]
 *  [GradleVersionMetric]
 *  [GradleBuildCacheModeMetric]
 *  [GradleBuildCachePushEnabled]
 *  [GradleScanLinkMetric]
 *
 * [gitMetrics] includes:
 *  [GitUserMetric]
 *  [GitBranchMetric]
 *
 * [performanceMetrics] includes:
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
 * [gradleSwitchesMetrics] includes:
 *  [GradleSwitchCachingMetric]
 *  [GradleSwitchBuildScanMetric]
 *  [GradleSwitchParallelMetric]
 *  [GradleSwitchConfigureOnDemandMetric]
 *  [GradleSwitchDryRunMetric]
 *  [GradleSwitchRefreshDependenciesMetric]
 *  [GradleSwitchRerunTasksMetric]
 *  [GradleSwitchDaemonMetric]
 *
 * [environmentMetrics] includes:
 *  [OsManufacturerMetric]
 *  [HostnameMetric]
 *  [PublicIpMetric]
 *  [DefaultCharsetMetric]
 *  [CacheHitMetric]
 *
 *  If you want to define custom metrics:
 *
 *  ```
 *  metrics {
 *    // Custom build metrics
 *    customBuildMetrics(
 *      "metricKey" to "metricValue"
 *    )
 *    // Custom task metrics
 *    customTaskMetrics(
 *      "metricKey" to "metricValue"
 *    )
 *    // Custom metric implementation
 *    customMetrics(
 *      MyCustomMetric()
 *    )
 *  }
 *  ```
 */
class MetricsConfiguration {
    /**
     * Flag to specify the generation of the unique build id.
     * In some cases could generate high cardinality problems like in basic InfluxDb setups, disabled by default
     */
    var generateBuildId = false
    var defaultMetrics = true
    var gitMetrics = true
    var performanceMetrics = true
    var gradleSwitchesMetrics = true
    var environmentMetrics = true


    private var metrics: MutableSet<Metric<*, *>> = mutableSetOf()

    private fun addDefaultMetrics() {
        with(metrics){
            add(RootProjectNameMetric())
            add(GradleRequestedTasksMetric())
            add(GradleVersionMetric())
            add(GradleBuildCacheModeMetric())
            add(GradleBuildCachePushEnabled())
            add(GradleScanLinkMetric())
        }
    }

    private fun addGitMetrics() {
        with(metrics) {
            add(GitUserMetric())
            add(GitBranchMetric())
        }
    }

    private fun addEnvironmentMetrics() {
        with(metrics) {
            add(OsManufacturerMetric())
            add(HostnameMetric())
            add(PublicIpMetric())
            add(DefaultCharsetMetric())
            add(CacheHitMetric())
        }
    }

    private fun addPerformanceMetrics() {
        with(metrics) {
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

        }
    }

    private fun addGradleSwitchesMetrics() {
        with(metrics) {
            add(GradleSwitchCachingMetric())
            add(GradleSwitchBuildScanMetric())
            add(GradleSwitchParallelMetric())
            add(GradleSwitchConfigureOnDemandMetric())
            add(GradleSwitchDryRunMetric())
            add(GradleSwitchRefreshDependenciesMetric())
            add(GradleSwitchRerunTasksMetric())
            add(GradleSwitchDaemonMetric())

        }
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

    /**
     * Adds the given custom build metrics into the metrics list.
     *
     * @param buildMetrics takes N [Pair]s to be added to the build metrics list.
     * You can find these metrics in the [ExecutionReport.customProperties].
     */
    fun customBuildMetrics(vararg buildMetrics: Pair<String, String>) {
        buildMetrics.mapTo(metrics) {
            createSimpleBuildMetric(it)
        }
    }

    /**
     * Adds the given custom build metric into the metrics list.
     *
     * @param buildMetric takes a [Pair] to be added to the build metrics list.
     * You can find this metric in the [ExecutionReport.customProperties].
     */
    fun customBuildMetrics(buildMetric: Pair<String, String>) {
        metrics.add(createSimpleBuildMetric(buildMetric))
    }

    /**
     * Adds the given custom build metrics into the metrics list.
     *
     * @param buildMetrics takes a [Map]s with metrics to be added to the build metrics list.
     * You can find these metrics in the [ExecutionReport.customProperties].
     */
    fun customBuildMetrics(buildMetrics: Map<String, String>) {
        buildMetrics.mapTo(metrics) {
            createSimpleBuildMetric(it.toPair())
        }
    }

    /**
     * Adds the given custom task metrics into the metrics list.
     *
     * @param taskMetrics takes N [Pair]s to be added to the task metrics list.
     * You can find these metrics in the [ExecutionReport.customProperties].
     */
    fun customTaskMetrics(vararg taskMetrics: Pair<String, String>) {
        taskMetrics.mapTo(metrics) {
            createSimpleTaskMetric(it)
        }
    }

    /**
     * Adds the given custom task metric into the metrics list.
     *
     * @param taskMetric takes a [Pair] to be added to the task metrics list.
     * You can find this metric in the [ExecutionReport.customProperties].
     */
    fun customTaskMetrics(taskMetric: Pair<String, String>) {
        metrics.add(createSimpleTaskMetric(taskMetric))
    }

    /**
     * Adds the given custom task metrics into the metrics list.
     *
     * @param taskMetrics takes a [Map]s with metrics to be added to the task metrics list.
     * You can find these metrics in the [ExecutionReport.customProperties].
     */
    fun customTaskMetrics(taskMetrics: Map<String, String>) {
        taskMetrics.mapTo(metrics) {
            createSimpleTaskMetric(it.toPair())
        }
    }

    internal fun build(): List<Metric<*, *>> {
        if (defaultMetrics) {
            addDefaultMetrics()
        }
        if (gitMetrics) {
            addGitMetrics()
        }
        if (performanceMetrics) {
            addPerformanceMetrics()
        }
        if (gradleSwitchesMetrics) {
            addGradleSwitchesMetrics()
        }
        if (environmentMetrics) {
            addEnvironmentMetrics()
        }
        if (generateBuildId) {
            metrics.add(BuildIdMetric())
        }

        return metrics.toList()
    }

    private fun createSimpleBuildMetric(pair: Pair<String, String>): SimpleMetric<String> {
        return SimpleMetric(
            provider = { pair.second },
            assigner = { report, value -> report.customProperties.buildProperties[pair.first] = value }
        )
    }

    private fun createSimpleTaskMetric(pair: Pair<String, String>): SimpleMetric<String> {
        return SimpleMetric(
            provider = { pair.second },
            assigner = { report, value -> report.customProperties.taskProperties[pair.first] = value }
        )
    }
}
