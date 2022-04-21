package io.github.cdsap.talaiot.publisher.pushgateway

import io.github.cdsap.talaiot.entities.ExecutionReport
import io.github.cdsap.talaiot.entities.TaskLength
import io.github.cdsap.talaiot.metrics.BuildMetrics
import io.github.cdsap.talaiot.metrics.TaskMetrics

class PushGatewayLabelProvider(val report: ExecutionReport) {
    private val customTaskLabelNames =
        report.customProperties.taskProperties.flatMap { listOf(it.key) }
    private val customTaskLabelValues =
        report.customProperties.taskProperties.flatMap { listOf(it.value) }
    private val customBuildLabelNames =
        report.customProperties.buildProperties.flatMap { listOf(it.key) }
    private val customBuildLabelValues =
        report.customProperties.buildProperties.flatMap { listOf(it.value) }

    fun taskLabelValues(task: TaskLength): List<String> {
        val labels = mutableListOf(
            task.module,
            task.critical.toString(),
            task.isLocalCacheHit.toString(),
            task.isRemoteCacheHit.toString(),
            task.state.name
        )
        labels.addAll(customTaskLabelValues)
        return labels
    }

    fun taskLabelNames(): List<String> {
        val taskNameMetrics = mutableListOf(
            TaskMetrics.Module.name,
            TaskMetrics.Critical.name,
            TaskMetrics.LocalCacheHit.name,
            TaskMetrics.RemoteCacheHit.name,
            TaskMetrics.State.name
        )
        taskNameMetrics.addAll(customTaskLabelNames)
        return taskNameMetrics
    }

    private fun checkBuildMetric(metrics: Map<String, Any>): List<BuildMetrics> {
        val list = mutableListOf<BuildMetrics>()
        if (metrics.containsKey(BuildMetrics.Hostname.toKey())) {
            list.add(BuildMetrics.Hostname)
        }
        if (metrics.containsKey(BuildMetrics.RequestedTasks.toKey())) {
            list.add(BuildMetrics.RequestedTasks)
        }
        if (metrics.containsKey(BuildMetrics.GitBranch.toKey())) {
            list.add(BuildMetrics.GitBranch)
        }
        if (metrics.containsKey(BuildMetrics.GitUser.toKey())) {
            list.add(BuildMetrics.GitUser)
        }
        return list
    }

    fun buildLabelNames(metrics: Map<String, Any>): List<String> {
        val buildLabelNames = checkBuildMetric(metrics)
        val labelNames = buildLabelNames.flatMap { listOf(it.toKey()) }.toMutableList()
        labelNames.addAll(customBuildLabelNames)
        return labelNames
    }

    fun buildLabelValues(metrics: Map<String, Any>): List<String> {
        val buildLabelNames = checkBuildMetric(metrics)
        val labelValues = buildLabelNames.flatMap { listOf(metrics[it.toKey()].toString()) }.toMutableList()
        labelValues.addAll(customBuildLabelValues)
        return labelValues
    }
}
