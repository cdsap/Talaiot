package io.github.cdsap.talaiot.provider
import io.github.cdsap.talaiot.configuration.MetricsConfiguration
import io.github.cdsap.talaiot.entities.ExecutedTasksInfo
import io.github.cdsap.talaiot.entities.ExecutionReport
import io.github.cdsap.talaiot.metrics.base.BuildResultMetric
import io.github.cdsap.talaiot.metrics.base.ExecutedTasksMetric
import org.gradle.BuildResult

/**
 * Provider for all metrics defined in the main [io.github.cdsap.talaiot.configuration.MetricsConfiguration].
 */
class MetricsPostBuildProvider(
    private val buildResult: BuildResult,
    /**
     * Information about all tasks that were executed
     */
    private val executedTasksInfo: ExecutedTasksInfo,
    private val metricsConfiguration: MetricsConfiguration,
    private val executionReport: ExecutionReport
) : Provider<ExecutionReport> {

    /**
     * Aggregates all metrics based on [io.github.cdsap.talaiot.configuration.MetricsConfiguration].
     *
     * @return execution report
     */
    override fun get(): ExecutionReport {

        val metrics = metricsConfiguration.build()

        /**
         * Could be optimized but for < 100 metrics performance shouldn't be an issue
         */
        metrics.forEach { metric ->
            when (metric) {
                is BuildResultMetric -> metric.get(buildResult, executionReport)
                is ExecutedTasksMetric -> metric.get(executedTasksInfo, executionReport)
            }
        }

        return executionReport
    }
}
