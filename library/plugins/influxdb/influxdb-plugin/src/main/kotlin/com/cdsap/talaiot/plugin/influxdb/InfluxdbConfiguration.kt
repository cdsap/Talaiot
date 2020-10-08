package com.cdsap.talaiot.plugin.influxdb

import com.cdsap.talaiot.publisher.influxdb.InfluxDbPublisherConfiguration
import com.cdsap.talaiot.publisher.PublishersConfiguration
import groovy.lang.Closure
import org.gradle.api.Project


class  InfluxdbConfiguration(project: Project) : PublishersConfiguration(project) {

    var influxDbPublisher: InfluxDbPublisherConfiguration? = null

    /**
     * Configuration accessor within the [PublishersConfiguration] for the [InfluxDbPublisher]
     *
     * @param configuration Configuration block for the [InfluxDbPublisherConfiguration]
     */
    fun influxDbPublisher(configuration: InfluxDbPublisherConfiguration.() -> Unit) {
        influxDbPublisher = InfluxDbPublisherConfiguration().also(configuration)
    }

    /**
     * Configuration accessor within the [PublishersConfiguration] for the [InfluxDbPublisher]
     *
     * @param closure closure for the [InfluxDbPublisherConfiguration]
     */
    fun influxDbPublisher(closure: Closure<*>) {
        influxDbPublisher = InfluxDbPublisherConfiguration()
        closure.delegate = influxDbPublisher
        closure.call()
    }
}