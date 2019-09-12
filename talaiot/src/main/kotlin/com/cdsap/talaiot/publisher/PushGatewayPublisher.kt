package com.cdsap.talaiot.publisher

import com.cdsap.talaiot.configuration.PushGatewayPublisherConfiguration
import com.cdsap.talaiot.entities.ExecutionReport
import com.cdsap.talaiot.logger.LogTracker
import com.cdsap.talaiot.request.Request
import java.util.concurrent.Executor

/**
 * Publisher using PushGateway format to send the metrics to an PushGateway server
 */
class PushGatewayPublisher(
    /**
     * General configuration for the publisher
     */
    private val pushGatewayPublisherConfiguration: PushGatewayPublisherConfiguration,
    /**
     * LogTracker to print in console depending on the Mode
     */
    private val logTracker: LogTracker,
    /**
     * Interface to send the measurements to an external service
     */
    private val requestPublisher: Request,
    /**
     * Executor to schedule a task in Background
     */
    private val executor: Executor
) : Publisher {
    private val TAG = "PushGatewayPublisher"

    fun getBuildMetricsContent(report: ExecutionReport): String {
        val buildTags =
                report.flattenBuildEnv()
                        .map { (k, v) -> "${k.formatTagPublisher().replace(".", "_")}=\"${v.formatTagPublisher()}\"" }
                        .joinToString(separator = ",")

        return "${pushGatewayPublisherConfiguration.buildJobName}{$buildTags} ${report.durationMs}"
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

            val taskFormatted = it.taskPath.formatTagPublisher().replace("-","_")
            contentTaskMetrics += "$taskFormatted{state=\"${it.state}\"" +
                    ",module=\"${it.module}\",rootNode=\"${it.rootNode}\" $properties} ${it.ms}\n"
        }

        return contentTaskMetrics
    }

    override fun publish(report: ExecutionReport) {

        if (pushGatewayPublisherConfiguration.url.isEmpty() ||
            pushGatewayPublisherConfiguration.taskJobName.isEmpty()
        ) {
            println(
                "PushGatewayPublisher not executed. Configuration requires url and taskJobName: \n" +
                        "pushGatewayPublisher {\n" +
                        "            url = \"http://localhost:9093\"\n" +
                        "            taskJobName = \"tracking\"\n" +
                        "}\n" +
                        "Please update your configuration"
            )

        } else {
            val url = pushGatewayPublisherConfiguration.url
            var contentTaskMetrics = ""
            var contentBuildMetrics = ""
            val urlTaskMetrics = "$url/metrics/job/${pushGatewayPublisherConfiguration.taskJobName}"
            val urlBuildMetrics = "$url/metrics/job/${pushGatewayPublisherConfiguration.buildJobName}"


            if (pushGatewayPublisherConfiguration.publishTaskMetrics) {
               contentTaskMetrics += getTaskMetricsContent(report)
            }

            if (pushGatewayPublisherConfiguration.publishBuildMetrics) {
                contentBuildMetrics += getBuildMetricsContent(report)
            }
            executor.execute {

                logTracker.log(TAG, "================")
                logTracker.log(TAG, "publishBuildMetrics: ${pushGatewayPublisherConfiguration.publishBuildMetrics}")
                logTracker.log(TAG, "publishTaskMetrics: ${pushGatewayPublisherConfiguration.publishTaskMetrics}")
                logTracker.log(TAG, "================")

                if (contentTaskMetrics.isNotBlank()) {
                    logTracker.log(TAG, "Inserting PushGateway Task metrics")
                    logTracker.log(TAG, "url: $urlTaskMetrics")
                    logTracker.log(TAG, "content: $contentTaskMetrics")
                    requestPublisher.send(urlTaskMetrics, contentTaskMetrics + "\n")
                }
                if (contentBuildMetrics.isNotBlank()) {
                    logTracker.log(TAG, "Inserting PushGateway Build metrics")
                    logTracker.log(TAG, "url: $urlBuildMetrics")
                    logTracker.log(TAG, "content: $contentBuildMetrics")
                    requestPublisher.send(urlBuildMetrics, contentBuildMetrics + "\n")
                }
            }
        }
    }
}
