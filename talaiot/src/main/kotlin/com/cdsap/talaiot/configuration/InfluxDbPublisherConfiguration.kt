package com.cdsap.talaiot.configuration

import groovy.lang.Closure


class InfluxDbPublisherConfiguration : PublisherConfiguration {
    override var name: String = "influxDb"
    var dbName: String = ""
    var url: String = ""
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
