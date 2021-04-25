package io.github.cdsap.talaiot.publisher.pushgateway

import io.github.cdsap.talaiot.entities.ExecutionReport
import io.github.cdsap.talaiot.metrics.DefaultBuildMetricsProvider
import io.github.cdsap.talaiot.metrics.DefaultTaskDataProvider

/**
 * Formatter to format task and build execution data to Pushgateway metrics format.
 */
class PushGatewayFormatter {

    fun getBuildMetricsContent(report: ExecutionReport, buildJobName: String): String {
        val fields = DefaultBuildMetricsProvider(report).get()
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

        report.tasks?.forEach {
            val values = DefaultTaskDataProvider(it, report)
                .get()
                .map { (k, v) ->
                    "${k.formatTagPublisher().replace(".", "_")}=\"${v.toString().formatTagPublisher()}\""
                }
                .joinToString(separator = ",")

            val taskFormatted = it.taskPath.formatTagPublisher().replace("-", "_")
            contentTaskMetrics += "$taskFormatted{$values} ${it.ms}\n"
        }

        return contentTaskMetrics
    }
}