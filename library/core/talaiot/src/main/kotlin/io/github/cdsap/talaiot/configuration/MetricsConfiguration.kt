package io.github.cdsap.talaiot.configuration

import io.github.cdsap.talaiot.entities.ExecutionReport
import io.github.cdsap.talaiot.metrics.BuildIdMetric
// import io.github.cdsap.talaiot.metrics.CacheHitMetric
import io.github.cdsap.talaiot.metrics.DefaultCharsetMetric
import io.github.cdsap.talaiot.metrics.GitBranchMetric
import io.github.cdsap.talaiot.metrics.GitUserMetric
import io.github.cdsap.talaiot.metrics.GradleMaxWorkersMetric
import io.github.cdsap.talaiot.metrics.GradleRequestedTasksMetric
import io.github.cdsap.talaiot.metrics.GradleScanLinkMetric
import io.github.cdsap.talaiot.metrics.GradleSwitchBuildScanMetric
import io.github.cdsap.talaiot.metrics.GradleSwitchCachingMetric
import io.github.cdsap.talaiot.metrics.GradleSwitchConfigurationCacheMetric
import io.github.cdsap.talaiot.metrics.GradleSwitchConfigureOnDemandMetric
import io.github.cdsap.talaiot.metrics.GradleSwitchDaemonMetric
import io.github.cdsap.talaiot.metrics.GradleSwitchDryRunMetric
import io.github.cdsap.talaiot.metrics.GradleSwitchParallelMetric
import io.github.cdsap.talaiot.metrics.GradleSwitchRefreshDependenciesMetric
import io.github.cdsap.talaiot.metrics.GradleSwitchRerunTasksMetric
import io.github.cdsap.talaiot.metrics.GradleVersionMetric
import io.github.cdsap.talaiot.metrics.HostnameMetric
import io.github.cdsap.talaiot.metrics.JavaVmNameMetric
import io.github.cdsap.talaiot.metrics.JvmMaxPermSizeMetric
import io.github.cdsap.talaiot.metrics.JvmXmsMetric
import io.github.cdsap.talaiot.metrics.JvmXmxMetric
import io.github.cdsap.talaiot.metrics.LocaleMetric
import io.github.cdsap.talaiot.metrics.OsMetric
import io.github.cdsap.talaiot.metrics.ProcessorCountMetric
import io.github.cdsap.talaiot.metrics.RootProjectNameMetric
import io.github.cdsap.talaiot.metrics.SimpleMetric
import io.github.cdsap.talaiot.metrics.UserMetric
import io.github.cdsap.talaiot.metrics.base.Metric

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
 *  [GradleSwitchConfigurationCacheMetric]
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
        with(metrics) {
            add(RootProjectNameMetric())
            add(GradleRequestedTasksMetric())
            add(GradleVersionMetric())
            add(GradleScanLinkMetric())
        }
    }

    private fun addGradleScanMetrics() {
        with(metrics) {
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
            add(HostnameMetric())
            add(DefaultCharsetMetric())
        }
    }

    private fun addPerformanceMetrics() {
        with(metrics) {
            add(UserMetric())
            add(OsMetric())
            add(ProcessorCountMetric())
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
            add(GradleSwitchConfigurationCacheMetric())
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
