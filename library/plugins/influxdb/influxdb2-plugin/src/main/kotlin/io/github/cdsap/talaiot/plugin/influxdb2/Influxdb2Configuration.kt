package io.github.cdsap.talaiot.plugin.influxdb2

import groovy.lang.Closure
import io.github.cdsap.talaiot.publisher.PublishersConfiguration
import io.github.cdsap.talaiot.publisher.influxdb2.InfluxDb2PublisherConfiguration
import org.gradle.api.Project

class Influxdb2Configuration(project: Project) : PublishersConfiguration(project) {

    var influxDb2Publisher: InfluxDb2PublisherConfiguration? = null

    /**
     * Configuration accessor within the [PublishersConfiguration] for the [InfluxDb2Publisher]
     *
     * @param configuration Configuration block for the [InfluxDb2PublisherConfiguration]
     */
    fun influxDb2Publisher(configuration: InfluxDb2PublisherConfiguration.() -> Unit) {
        influxDb2Publisher = InfluxDb2PublisherConfiguration().also(configuration)
    }

    /**
     * Configuration accessor within the [PublishersConfiguration] for the [InfluxDb2Publisher]
     *
     * @param closure closure for the [InfluxDb2PublisherConfiguration]
     */
    fun influxDb2Publisher(closure: Closure<*>) {
        influxDb2Publisher = InfluxDb2PublisherConfiguration()
        closure.delegate = influxDb2Publisher
        closure.call()
    }
}
