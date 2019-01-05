package com.cdsap.talaiot.configuration


import com.cdsap.talaiot.publisher.Publisher
import com.cdsap.talaiot.publisher.OutputPublisher

class PublishersConfiguration {
    var influxDbPublisher: InfluxDbPublisherConfiguration? = null
    var outputPublisher: OutputPublisher? = null
    var customPublisher: Publisher? = null

    fun influxDbPublisher(configuration: InfluxDbPublisherConfiguration.() -> Unit) {
        influxDbPublisher = InfluxDbPublisherConfiguration().also(configuration)
    }

    fun customPublisher(configuration: Publisher) {
        customPublisher = configuration
    }


    // Groovy compat
    //fun outputPublisher(closure: Closure<*>) {
    //    outputPublisher = OutputPublisher()
    //    closure.delegate = outputPublisher
    //    closure.call()
    //}
}