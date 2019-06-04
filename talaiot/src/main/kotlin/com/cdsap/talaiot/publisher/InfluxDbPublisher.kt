package com.cdsap.talaiot.publisher

import com.cdsap.talaiot.configuration.InfluxDbPublisherConfiguration
import com.cdsap.talaiot.entities.TaskMeasurementAggregated
import com.cdsap.talaiot.filter.TaskFilterProcessor
import com.cdsap.talaiot.logger.LogTracker
import com.cdsap.talaiot.request.Request
import java.util.concurrent.Executor

/**
 * Publisher using InfluxDb and LineProtocol format to send the metrics
 */
class InfluxDbPublisher(
    /**
     * General configuration for the publisher
     */
    private val influxDbPublisherConfiguration: InfluxDbPublisherConfiguration,
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

    val filtering = TaskFilterProcessor(logTracker,influxDbPublisherConfiguration.filter)

    override fun publish(taskMeasurementAggregated: TaskMeasurementAggregated) {
        logTracker.log("================")
        logTracker.log("InfluxDbPublisher")
        logTracker.log("================")
        if (influxDbPublisherConfiguration.url.isEmpty() ||
            influxDbPublisherConfiguration.dbName.isEmpty() ||
            influxDbPublisherConfiguration.urlMetric.isEmpty()
        ) {
            println(
                "InfluxDbPublisher not executed. Configuration requires url, dbName and urlMetrics: \n" +
                        "influxDbPublisher {\n" +
                        "            dbName = \"tracking\"\n" +
                        "            url = \"http://localhost:8086\"\n" +
                        "            urlMetric = \"tracking\"\n" +
                        "}\n" +
                        "Please update your configuration"
            )

        } else {
            val url = "${influxDbPublisherConfiguration.url}/write?db=${influxDbPublisherConfiguration.dbName}"
            var content = ""

            taskMeasurementAggregated.apply {
                var metrics = ""
                values.forEach {
                    val tag = formatToLineProtocol(it.key)
                    val tagValue = formatToLineProtocol(it.value)
                    metrics += "$tag=$tagValue,"
                }
                taskMeasurement
                    .filter { filtering.taskLengthFilter(it) }
                    .forEach {
                        content += "${influxDbPublisherConfiguration.urlMetric},state=${it.state}" +
                                ",module=${it.module},rootNode=${it.rootNode},task=${it.taskPath},${metrics.dropLast(1)} value=${it.ms}\n"
                    }
                logTracker.log(content)
            }

            if (!content.isEmpty()) {
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
