package com.cdsap.talaiot.configuration


/**
 * Configuration for the [ElasticSearchPublisher]. It belongs to the Publisher configurations
 *
 * elasticSearhcPublisher {
 *    url = "url"
 *    taskIndexName = "tracking"
 *    buildIndexName = "buildMetric"
 *
 * }
 */
class ElasticSearchPublisherConfiguration : PublisherConfiguration {

    /**
     * name of the publisher
     */
    override var name: String = "elasticSearch"

    override var publishBuildMetrics: Boolean = true
    override var publishTaskMetrics: Boolean = true
    /**
     * url from the InfluxDb instance required to send the measurements. For instance http://localhost:8086
     */
    var url: String = ""

    /**
     * metric to identify the measurement in InfluxDb
     */
    var taskIndexName: String = "task"
    /**
     * metric name to identify the build measurements in InfluxDb
     */
    var buildIndexName: String = "build"
}
