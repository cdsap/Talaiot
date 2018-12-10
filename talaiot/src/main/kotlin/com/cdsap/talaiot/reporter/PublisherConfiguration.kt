package com.cdsap.talaiot.reporter

class PublisherConfiguration {
    var influxDb: InfluxDbPublisherConfiguration? = null
    var output: OutputPublisher = OutputPublisher()


    fun influxDb(configuration: InfluxDbPublisherConfiguration.() -> Unit) {
        influxDb = InfluxDbPublisherConfiguration().also(configuration)
    }

    fun output(configuration: OutputPublisher.() -> Unit) {
        output = OutputPublisher().also(configuration)
    }

}