package com.cdsap.talaiot.reporter

class PublisherConfiguration {
    var influxDbPublisher: InfluxDbPublisherConfiguration? = null
    var outputPublisher: OutputPublisher = OutputPublisher()


    fun influxDbPublisher(configuration: InfluxDbPublisherConfiguration.() -> Unit) {
        influxDbPublisher = InfluxDbPublisherConfiguration().also(configuration)
    }

    fun outputPublisher(configuration: OutputPublisher.() -> Unit) {
        outputPublisher = OutputPublisher().also(configuration)
    }

}