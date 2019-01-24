package com.cdsap.talaiot.publisher

import com.cdsap.talaiot.configuration.InfluxDbPublisherConfiguration
import com.cdsap.talaiot.entities.TaskMeasurementAggregated
import com.cdsap.talaiot.logger.LogTracker
import com.cdsap.talaiot.request.Request


class InfluxDbPublisher(
    private val influxDbPublisherConfiguration: InfluxDbPublisherConfiguration,
    private val logTracker: LogTracker,
    private val requestPublisher: Request
) :
    Publisher {

    override fun publish(measurementAggregated: TaskMeasurementAggregated) {
        val url = "${influxDbPublisherConfiguration.url}/write?db=${influxDbPublisherConfiguration.dbName}"
        var content = ""

        measurementAggregated.apply {
            logTracker.log("================")
            logTracker.log("HttpReporting")
            logTracker.log("================")
            var metrics = ""
            this.values.forEach {
                metrics += "${it.key}=\"${it.value}\","
            }
            this.taskMeasurement.forEach {
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