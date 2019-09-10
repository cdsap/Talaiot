package com.cdsap.talaiot.configuration


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

}