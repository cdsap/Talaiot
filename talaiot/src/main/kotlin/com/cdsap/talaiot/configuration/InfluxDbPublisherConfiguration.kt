package com.cdsap.talaiot.configuration

import groovy.lang.Closure

/**
 * Configuration for the InfluxDbPublisher. It belongs to the Publisher configurations
 *
 * influxDbPublisher {
 *    dbName = "tracking"
 *    url = "url"
 *    urlMetric = "tracking
 * }
 */
class InfluxDbPublisherConfiguration : PublisherConfiguration {
    /**
     * name of the publisher
     */
    override var name: String = "influxDb"
    /**
     * name of the InfluxDb database, it should be created before the first tracking
     */
    var dbName: String = ""
    /**
     * url from the InfluxDb instance required to send the measurements. For instance http://localhost:8086
     */
    var url: String = ""
    /**
     * metric to identify the measurement in InfluxDb
     */
    var urlMetric: String = ""
    var threshold: ThresholdConfiguration? = null

    fun threshold(configuration: ThresholdConfiguration.() -> Unit) {
        threshold = ThresholdConfiguration().also(configuration)
    }

    fun threshold(closure: Closure<*>) {
        threshold = ThresholdConfiguration()
        closure.delegate = threshold
        closure.call()
    }
}
