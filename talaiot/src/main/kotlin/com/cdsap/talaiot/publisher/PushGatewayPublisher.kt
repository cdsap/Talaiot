package com.cdsap.talaiot.publisher

import com.cdsap.talaiot.configuration.InfluxDbPublisherConfiguration
import com.cdsap.talaiot.configuration.PushGatewayPublisherConfiguration
import com.cdsap.talaiot.entities.TaskMeasurementAggregated
import com.cdsap.talaiot.logger.LogTracker
import com.cdsap.talaiot.request.Request
import java.util.concurrent.Executor

/**
 * Publisher using PushGateway and LineProtocol format to send the metrics
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

    override fun publish(taskMeasurementAggregated: TaskMeasurementAggregated) {
        logTracker.log("================")
        logTracker.log("PushGatewayPublisher")
        logTracker.log("================")
        if (pushGatewayPublisherConfiguration.url.isEmpty() ||
            pushGatewayPublisherConfiguration.nameJob.isEmpty()
        ) {
            println(
                "PushGatewayPublisher not executed. Configuration requires url and urlMetrics: \n" +
                        "pushGatewayPublisher {\n" +
                        "            url = \"http://localhost:9093\"\n" +
                        "            urlMetric = \"tracking\"\n" +
                        "}\n" +
                        "Please update your configuration"
            )

        } else {
            val url =
                "${pushGatewayPublisherConfiguration.url}/metrics/job/${pushGatewayPublisherConfiguration.nameJob}"
            var content = ""

            taskMeasurementAggregated.apply {
                var metrics = ""
                values.forEach {
                    val tag = formatToLineProtocol(it.key)
                    val tagValue = formatToLineProtocol(it.value)
                    metrics += "$tag=\"$tagValue\","
                }
                taskMeasurement
                    .forEach {
                        content += "${it.taskPath}{state=\"${it.state}\"" +
                                ",module=\"${it.module}\",rootNode=\"${it.rootNode}\",${metrics.dropLast(1)}} ${it.ms}\n"


                    }
                logTracker.log(content)
            }

            println("1212121")
            if (!content.isEmpty()) {
                println("33333")
                executor.execute {
                    requestPublisher.send(url, content)
                }
            } else {
                logTracker.log("Empty content")
            }
        }
    }

    /**
     * Influx Line Protocol requires specific format, we need to replace values like ","
     * @param tag value to be formatted
     *
     * @return value formatted
     */
    private fun formatToLineProtocol(tag: String) = tag.replace(Regex("""[ ,=,\,]"""), "")

}
