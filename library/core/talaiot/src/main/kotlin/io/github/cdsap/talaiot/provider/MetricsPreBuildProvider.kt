package io.github.cdsap.talaiot.provider

import io.github.cdsap.talaiot.TalaiotExtension
import io.github.cdsap.talaiot.configuration.MetricsConfiguration
import io.github.cdsap.talaiot.entities.ExecutionReport
import io.github.cdsap.talaiot.metrics.SimpleMetric
import io.github.cdsap.talaiot.metrics.base.GradleMetric
import org.gradle.api.Project

class MetricsPreBuildProvider(
    /**
     * Gradle project required to access [TalaiotExtension]
     */
    private val project: Project,
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
                is GradleMetric -> metric.get(project, executionReport)
                is SimpleMetric -> metric.get(Unit, executionReport)
            }
        }

        return executionReport
    }
}
