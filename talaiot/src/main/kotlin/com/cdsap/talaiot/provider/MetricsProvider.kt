package com.cdsap.talaiot.provider

import com.cdsap.talaiot.TalaiotExtension
import com.cdsap.talaiot.entities.ExecutionReport
import com.cdsap.talaiot.metrics.*
import com.cdsap.talaiot.metrics.base.GradleMetric
import org.gradle.api.Project

/**
 * Provider for all metrics defined in the main MetricsConfiguration
 */
class MetricsProvider(
    /**
     * Gradle project required to access to the TalaiotExtension
     */
    private val project: Project
) : Provider<ExecutionReport> {

    /**
     * Aggregates all metrics depending if there are enabled in the MetricsConfiguration.
     * It access trough the Metrics interface
     *
     * @return collection of Pairs.
     */
    override fun get(): ExecutionReport {
        val report = ExecutionReport()

        val talaiotExtension = project.extensions.getByName("talaiot") as TalaiotExtension
        val metrics = talaiotExtension.metrics.build()

        /**
         * Could be optimized but for < 100 metrics performance shouldn't be an issue
         */
        metrics.forEach { metric ->
            when (metric) {
                is GradleMetric -> metric.get(project, report)
                is SimpleMetric -> metric.get(Unit, report)
            }
        }

        return report
    }
}
