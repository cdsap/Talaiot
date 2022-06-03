package io.github.cdsap.talaiot.provider

import io.github.cdsap.talaiot.entities.ExecutionReport
import io.github.cdsap.talaiot.metrics.SimpleMetric
import io.github.cdsap.talaiot.metrics.base.Metric
import org.gradle.api.Project

class MetricsPreBuildProvider(
    private val project: Project,
    private val metrics: List<Metric<*, *>>,
    private val executionReport: ExecutionReport
) : Provider<ExecutionReport> {

    override fun get(): ExecutionReport {
        metrics.forEach { metric ->
            when (metric) {
                is SimpleMetric -> metric.get(Unit, executionReport)
            }
        }
        return executionReport
    }
}
