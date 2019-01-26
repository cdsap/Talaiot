package com.cdsap.talaiot.configuration


import com.cdsap.talaiot.publisher.Publisher
import com.cdsap.talaiot.publisher.OutputPublisher
import groovy.lang.Closure

class PublishersConfiguration {
    var influxDbPublisher: InfluxDbPublisherConfiguration? = null
    var outputPublisher: Boolean? = null
    var customPublisher: Publisher? = null

    fun influxDbPublisher(configuration: InfluxDbPublisherConfiguration.() -> Unit) {
        influxDbPublisher = InfluxDbPublisherConfiguration().also(configuration)
    }

//    fun outputPublisher(configuration: OutputPublisherConfiguration.() -> Unit) {
//        outputPublisher = OutputPublisherConfiguration().also(configuration)
//    }

    fun customPublisher(configuration: Publisher) {
        customPublisher = configuration
    }

    fun influxDbPublisher(closure: Closure<*>) {
        influxDbPublisher = InfluxDbPublisherConfiguration()
        closure.delegate = influxDbPublisher
        closure.call()
    }

//    fun outputPublisher(closure: Closure<*>) {
//        outputPublisher = OutputPublisherConfiguration()
//        closure.delegate = outputPublisher
//        closure.call()
//    }
}