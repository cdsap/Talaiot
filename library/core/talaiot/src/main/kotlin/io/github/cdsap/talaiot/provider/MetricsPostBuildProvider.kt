package io.github.cdsap.talaiot.provider

import io.github.cdsap.talaiot.entities.ExecutedTasksInfo
import io.github.cdsap.talaiot.entities.ExecutionReport
import io.github.cdsap.talaiot.metrics.base.BuildResultMetric
import io.github.cdsap.talaiot.metrics.base.ExecutedTasksMetric
import io.github.cdsap.talaiot.metrics.base.GradleMetric
import io.github.cdsap.talaiot.metrics.base.Metric
import org.gradle.BuildResult
import org.gradle.api.Project

class MetricsPostBuildProvider(
    private val buildResult: BuildResult,
    private val executedTasksInfo: ExecutedTasksInfo,
    private val metrics: List<Metric<*, *>>,
    private val executionReport: ExecutionReport,
    private val project: Project
) : Provider<ExecutionReport> {

    override fun get(): ExecutionReport {
        metrics.forEach { metric ->
            when (metric) {
                is BuildResultMetric -> metric.get(buildResult, executionReport)
                is ExecutedTasksMetric -> metric.get(executedTasksInfo, executionReport)
                is GradleMetric -> metric.get(project, executionReport)
            }
        }
        return executionReport
    }
}
