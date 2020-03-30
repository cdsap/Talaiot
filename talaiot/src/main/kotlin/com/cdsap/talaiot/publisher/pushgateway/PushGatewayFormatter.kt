package com.cdsap.talaiot.publisher.pushgateway

import com.cdsap.talaiot.entities.ExecutionReport
import com.cdsap.talaiot.metrics.DefaultBuildMetricsProvider
import com.cdsap.talaiot.metrics.DefaultTaskDataProvider
import com.cdsap.talaiot.publisher.formatTagPublisher

/**
 * Formatter to format task and build execution data to Pushgateway metrics format.
 */
class PushGatewayFormatter {

    fun getBuildMetricsContent(report: ExecutionReport, buildJobName: String): String {
        val fields = report.flattenBuildEnv() + DefaultBuildMetricsProvider(report).get()
        val buildTags =
            fields
                .map { (k, v) ->
                    "${k.formatTagPublisher().replace(".", "_")}=\"${v.toString().formatTagPublisher()}\""
                }
                .joinToString(separator = ",")
        return "${buildJobName}{$buildTags} ${report.durationMs}"
    }

    fun getTaskMetricsContent(report: ExecutionReport): String {
        var contentTaskMetrics = ""
        val taskProperties = report.customProperties.taskProperties.map { (k, v) ->
            "${k.formatTagPublisher().replace(
                ".",
                "_"
            )}=\"${v.formatTagPublisher()}\""
        }.joinToString(separator = ",")

        val properties = if (taskProperties.isNotBlank()) ",$taskProperties" else ""

        report.tasks?.forEach {
            val values = DefaultTaskDataProvider(it)
                .get()
                .map { (k, v) ->
                    "${k.formatTagPublisher().replace(".", "_")}=\"${v.toString().formatTagPublisher()}\""
                }
                .joinToString(separator = ",")

            val taskFormatted = it.taskPath.formatTagPublisher().replace("-", "_")
            contentTaskMetrics += "$taskFormatted{$values $properties} ${it.ms}\n"
        }

        return contentTaskMetrics
    }
}