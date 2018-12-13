package com.cdsap.talaiot.publisher.influxdb

import com.cdsap.talaiot.entities.TaskMeasurementAggregated
import com.cdsap.talaiot.logger.LogTracking
import com.cdsap.talaiot.request.Request
import com.cdsap.talaiot.publisher.Publisher


class InfluxDbPublisher(
    private val influxDbPublisherConfiguration: InfluxDbPublisherConfiguration
) :
    Publisher {
    override var logTracker = LogTracking(LogTracking.Mode.INFO)

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
                content += "${influxDbPublisherConfiguration.urlMetric},task=\"${it.path}\",${metrics.dropLast(1)}  value=${it.ms}   \n"
            }
            logTracker.log("$content")
        }
        Request(url, content, logTracker)
    }

}