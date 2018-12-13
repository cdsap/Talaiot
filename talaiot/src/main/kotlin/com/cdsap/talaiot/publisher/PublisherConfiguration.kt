package com.cdsap.talaiot.publisher

import com.cdsap.talaiot.publisher.influxdb.InfluxDbPublisherConfiguration
import com.cdsap.talaiot.publisher.output.OutputPublisher

class PublisherConfiguration {
    var influxDbPublisher: InfluxDbPublisherConfiguration? = null
    var outputPublisher: OutputPublisher =
        OutputPublisher()


    fun influxDbPublisher(configuration: InfluxDbPublisherConfiguration.() -> Unit) {
        influxDbPublisher = InfluxDbPublisherConfiguration().also(configuration)
    }

    fun outputPublisher(configuration: OutputPublisher.() -> Unit) {
        outputPublisher = OutputPublisher().also(configuration)
    }

}