package com.cdsap.talaiot.configuration

import groovy.lang.Closure

/**
 * Configuration for the InfluxDbPublisher. It belongs to the Publisher configurations
 *
 * influxDbPublisher {
 *    dbName = "tracking"
 *    url = "url"
 *    taskMetricName = "tracking"
 *    buildMetricName = "buildMetric"
 *
 * }
 */
class ElasticSearchPublisherConfiguration : PublisherConfiguration {

    /**
     * name of the publisher
     */
    override var name: String = "elasticSearch"
    /**
     * name of the InfluxDb database, will be automatically created
     */
    var dbName: String = ""
    /**
     * url from the InfluxDb instance required to send the measurements. For instance http://localhost:8086
     */
    var url: String = ""
    var port: String = ""

    /**
     * metric to identify the measurement in InfluxDb
     */
    var taskMetricName: String = "task"
    /**
     * metric name to identify the build measurements in InfluxDb
     */
    var buildMetricName: String = "build"
    /**
     * optional username for authentication
     */
    var username: String = ""
    /**
     * optional password for authorization
     */
    var password: String = ""


}
