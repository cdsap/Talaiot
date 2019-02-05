package com.cdsap.talaiot.publisher

import com.cdsap.talaiot.configuration.InfluxDbPublisherConfiguration
import com.cdsap.talaiot.entities.TaskMeasurementAggregated
import com.cdsap.talaiot.logger.LogTracker
import com.cdsap.talaiot.request.Request


class InfluxDbPublisher(
    private val influxDbPublisherConfiguration: InfluxDbPublisherConfiguration,
    private val logTracker: LogTracker,
    private val requestPublisher: Request
) : Publisher {

    override fun publish(measurementAggregated: TaskMeasurementAggregated) {
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

            measurementAggregated.apply {
                var metrics = ""
                values.forEach {
                    metrics += "${it.key}=\"${it.value}\","
                }
                taskMeasurement.forEach {
                    content += "${influxDbPublisherConfiguration.urlMetric},state=\"${it.state}\"" +
                            ",task=\"${it.taskName}\",${metrics.dropLast(1)}  value=${it.ms}\n"
                }
                logTracker.log(content)
            }
            if (!content.isEmpty()) {
                requestPublisher.send(url, content)
            } else {
                logTracker.log("Empty content")
            }
        }
    }

}