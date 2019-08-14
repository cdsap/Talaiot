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

    override fun publish(report: ExecutionReport) {
        logTracker.log("================")
        logTracker.log("PushGatewayPublisher")
        logTracker.log("publishBuildMetrics: ${pushGatewayPublisherConfiguration.publishBuildMetrics}")
        logTracker.log("publishTaskMetrics: ${pushGatewayPublisherConfiguration.publishTaskMetrics}")
        logTracker.log("================")

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
            val url =
                "${pushGatewayPublisherConfiguration.url}/metrics/job/${pushGatewayPublisherConfiguration.taskJobName}"
            var content = ""

            if (pushGatewayPublisherConfiguration.publishTaskMetrics) {
                report.tasks?.forEach {
                    content += "${it.taskPath}{state=\"${it.state}\"" +
                            ",module=\"${it.module}\",rootNode=\"${it.rootNode}\"} ${it.ms}\n"
                    logTracker.log(content)
                }
            }

            if (pushGatewayPublisherConfiguration.publishBuildMetrics) {
                val buildTags =
                    report.flattenBuildEnv()
                        .map { (k, v) -> "${k.formatTagPublisher()}=${v.formatTagPublisher()}" }
                        .joinToString(separator = ",")
                content += "${pushGatewayPublisherConfiguration.buildJobName}{$buildTags} ${report.durationMs}"

            }

            if (content.isNotEmpty()) {
                executor.execute {
                    requestPublisher.send(url, content)
                }
            } else {
                logTracker.log("Empty content")
            }
        }
    }
}
