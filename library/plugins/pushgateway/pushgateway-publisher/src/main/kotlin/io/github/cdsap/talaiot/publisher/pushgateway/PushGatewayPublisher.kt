package io.github.cdsap.talaiot.publisher.pushgateway

import io.github.cdsap.talaiot.entities.ExecutionReport
import io.github.cdsap.talaiot.logger.LogTracker
import io.github.cdsap.talaiot.publisher.Publisher
import io.prometheus.client.CollectorRegistry
import io.prometheus.client.exporter.PushGateway
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
     * Executor to schedule a task in Background
     */
    private val executor: Executor

) : Publisher {
    private val TAG = "PushGatewayPublisher"

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
            val urlTaskMetrics = "$url/metrics/job/${pushGatewayPublisherConfiguration.taskJobName}"
            val urlBuildMetrics = "$url/metrics/job/${pushGatewayPublisherConfiguration.buildJobName}"
            val urlNoProtocol = url.replace("https://", "").replace("http://", "")
            val pushgatewayLabelProvider = PushGatewayLabelProvider(report)

            executor.execute {

                logTracker.log(TAG, "================")
                logTracker.log(TAG, "publishBuildMetrics: ${pushGatewayPublisherConfiguration.publishBuildMetrics}")
                logTracker.log(TAG, "publishTaskMetrics: ${pushGatewayPublisherConfiguration.publishTaskMetrics}")
                logTracker.log(TAG, "================")

                if (pushGatewayPublisherConfiguration.publishTaskMetrics) {
                    logTracker.log(TAG, "Inserting PushGateway Task metrics")
                    logTracker.log(TAG, "url: $urlBuildMetrics")

                    val registry = CollectorRegistry()
                    PushGatewayTaskCollector(report, registry, pushgatewayLabelProvider)
                        .collect()
                    PushGateway(urlNoProtocol)
                        .pushAdd(registry, pushGatewayPublisherConfiguration.taskJobName)
                }

                if (pushGatewayPublisherConfiguration.publishBuildMetrics) {
                    logTracker.log(TAG, "Inserting PushGateway Build metrics")
                    logTracker.log(TAG, "url: $urlTaskMetrics")

                    val registry = CollectorRegistry()
                    PushGatewayBuildCollector(report, registry, pushgatewayLabelProvider)
                        .collect()
                    PushGateway(urlNoProtocol)
                        .pushAdd(registry, pushGatewayPublisherConfiguration.buildJobName)
                }
            }
        }
    }
}
