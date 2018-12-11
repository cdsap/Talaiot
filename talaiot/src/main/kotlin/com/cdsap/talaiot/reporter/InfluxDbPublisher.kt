package com.cdsap.talaiot.reporter

import com.cdsap.talaiot.entities.TaskMeasurementAggregated
import com.cdsap.talaiot.logger.LogTracking


class InfluxDbPublisher(private val influxDbPublisherConfiguration: InfluxDbPublisherConfiguration) : Reporter {
    override var logTracker = LogTracking(LogTracking.Mode.INFO)

    override fun send(measurementAggregated: TaskMeasurementAggregated) {
        val url = "${influxDbPublisherConfiguration.url}/write?db=${influxDbPublisherConfiguration.dbName}"
        var content = ""

        measurementAggregated.apply {
            logTracker.log("================")
            logTracker.log("HttpReporting")
            logTracker.log("================")
            logTracker.log("User ${this.user}")
            logTracker.log("Branch ${this.branch}")
            logTracker.log("Os ${this.os}")
            logTracker.log("Project ${this.project}")
            logTracker.log("Gradle Version ${this.gradleVersion}")

            this.taskMeasurement.forEach {

                content += "${influxDbPublisherConfiguration.urlMetric},task=\"${it.path}\"," +
                        "user=\"${this.user}\"," +
                        "totalMemory=\"${this.totalMemory}\"," +
                        "freeMemory=\"${this.freeMemory}\"," +
                        "project=\"${this.project}\"," +
                        "state=\"${it.state}\"," +
                        "maxMemory=\"${this.maxMemory}\"," +
                        "availableProcessors=\"${this.availableProcessors}\"," +
                        "branch=\"${this.branch}\"," +
                        "gradleVerion=\"${this.gradleVersion}\"," +
                        "os=\"${this.os}\" value=${it.ms}   \n"
            }
            logTracker.log("$content")
        }
        Call(url, content, logTracker)
    }

}