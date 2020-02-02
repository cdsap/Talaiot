package com.cdsap.talaiot.configuration

import com.cdsap.talaiot.publisher.InfluxDbPublisher
import groovy.lang.Closure

/**
 * Configuration for the [RethinkDbPublisher]. It belongs to the Publisher configurations
 *
 * influxDbPublisher {
 *    dbName = "tracking"
 *    url = "url"
 *    taskMetricName = "tracking"
 *    buildMetricName = "buildMetric"
 *
 * }
 */

/**
 * Configuration for the [RethinkDbPublisher]. It belongs to the Publisher configurations
 *
 * influxDbPublisher {
 *    dbName = "tracking"
 *    url = "url"
 *    taskMetricName = "tracking"
 *    buildMetricName = "buildMetric"
 *
 * }
 */
class RethinkDbPublisherConfiguration : PublisherConfiguration {

    override var publishBuildMetrics: Boolean = true
    override var publishTaskMetrics: Boolean = true

    /**
     * name of the publisher
     */
    override var name: String = "influxDb"
    /**
     * name of the InfluxDb database, will be automatically created
     */
    var dbName: String = ""
    /**
     * url from the InfluxDb instance required to send the measurements. For instance http://localhost:8086
     */
    var url: String = ""
    /**
     * metric to identify the measurement in InfluxDb
     */
    var taskTableName: String = "task"
    /**
     * metric name to identify the build measurements in InfluxDb
     */
    var buildTableName: String = "build"
    /**
     * optional username for authentication
     */
    var username: String = ""
    /**
     * optional password for authorization
     */
    var password: String = ""


}
