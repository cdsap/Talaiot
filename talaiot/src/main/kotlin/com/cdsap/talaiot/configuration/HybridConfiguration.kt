package com.cdsap.talaiot.configuration

import groovy.lang.Closure


class HybridConfiguration : PublisherConfiguration {
    override var name: String = "hybrid"

    override var publishBuildMetrics: Boolean = true
    override var publishTaskMetrics: Boolean = true

    var publisher: PublisherConfiguration? = null

    fun influxDbPublisher(configuration: InfluxDbPublisherConfiguration.() -> Unit) {
        publisher = InfluxDbPublisherConfiguration().also(configuration)
    }

    fun customPublisher(configuration: CustomPublisherConfiguration.() -> Unit) {
        publisher = CustomPublisherConfiguration().also(configuration)
    }

    fun pushGatewayPublisher(configuration: PushGatewayPublisherConfiguration.() -> Unit) {
        publisher = PushGatewayPublisherConfiguration().also(configuration)
    }

    fun elasticSearchPublisher(configuration: ElasticSearchPublisherConfiguration.() -> Unit) {
        publisher = ElasticSearchPublisherConfiguration().also(configuration)
    }

    fun influxDbPublisher(closure: Closure<*>) {
        publisher = InfluxDbPublisherConfiguration()
        closure.delegate = publisher
        closure.call()
    }

    fun customPublisher(closure: Closure<*>) {
        publisher = CustomPublisherConfiguration()
        closure.delegate = publisher
        closure.call()
    }

    fun pushGatewayPublisher(closure: Closure<*>) {
        publisher = PushGatewayPublisherConfiguration()
        closure.delegate = publisher
        closure.call()
    }

    fun elasticSearchPublisher(closure: Closure<*>) {
        publisher = ElasticSearchPublisherConfiguration()
        closure.delegate = publisher
        closure.call()
    }
}