package com.cdsap.talaiot.provider

import com.cdsap.talaiot.TalaiotExtension
import com.cdsap.talaiot.entities.ExecutionReport
import com.cdsap.talaiot.metrics.SimpleMetric
import com.cdsap.talaiot.metrics.base.BuildResultMetric
import com.cdsap.talaiot.metrics.base.GradleMetric
import org.gradle.BuildResult
import org.gradle.api.Project

/**
 * Provider for all metrics defined in the main [com.cdsap.talaiot.configuration.MetricsConfiguration].
 */
class MetricsProvider(
    /**
     * Gradle project required to access [TalaiotExtension]
     */
    private val project: Project,
    private val buildResult: BuildResult
) : Provider<ExecutionReport> {

    /**
     * Aggregates all metrics based on [com.cdsap.talaiot.configuration.MetricsConfiguration].
     *
     * @return execution report
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
                is BuildResultMetric -> metric.get(buildResult, report)
            }
        }

        return report
    }
}
