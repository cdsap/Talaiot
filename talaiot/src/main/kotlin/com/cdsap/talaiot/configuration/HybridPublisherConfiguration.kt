package com.cdsap.talaiot.configuration

import com.cdsap.talaiot.publisher.HybridPublisher

/**
 * Configuration for the [HybridPublisher]. It belongs to the Publisher configurations
 *
 * hybridPublisher {
 *
 *    taskPublisher {
 *       influxDbPublisher {
 *       }
 *    }
 *    buildPublisher {
 *       pushGatewayPublisher {
 *       }
 *    }
 *
 * }
 */
class HybridPublisherConfiguration : PublisherConfiguration {

    /**
     * name of the publisher
     */
    override var name: String = "hybrid"

    override var publishBuildMetrics: Boolean = true
    override var publishTaskMetrics: Boolean = true

    /**
     * Publisher used to report only tasks metrics
     */
    var taskPublisher: PublisherConfiguration? = null

    /**
     * Publisher used to report only build metrics
     */
    var buildPublisher: PublisherConfiguration? = null

    @JvmName("taskPublisherInflux")
    fun taskPublisher(configuration: InfluxDbPublisherConfiguration.() -> Unit) {
        taskPublisher = InfluxDbPublisherConfiguration().also(configuration)
    }

    @JvmName("taskPublisherPush")
    fun taskPublisher(configuration: PushGatewayPublisherConfiguration.() -> Unit) {
        taskPublisher = PushGatewayPublisherConfiguration().also(configuration)
    }

    @JvmName("taskPublisherElastic")
    fun taskPublisher(configuration: ElasticSearchPublisherConfiguration.() -> Unit) {
        taskPublisher = ElasticSearchPublisherConfiguration().also(configuration)
    }

    @JvmName("taskPublisherCustomConfiguration")
    fun taskPublisher(configuration: CustomPublisherConfiguration.() -> Unit) {
        taskPublisher = CustomPublisherConfiguration().also(configuration)
    }

    @JvmName("buildPublisherInflux")
    fun buildPublisher(configuration: InfluxDbPublisherConfiguration.() -> Unit) {
        taskPublisher = InfluxDbPublisherConfiguration().also(configuration)
    }

    @JvmName("buildPublisherPush")
    fun buildPublisher(configuration: PushGatewayPublisherConfiguration.() -> Unit) {
        taskPublisher = PushGatewayPublisherConfiguration().also(configuration)
    }

    @JvmName("buildPublisherElastic")
    fun buildPublisher(configuration: ElasticSearchPublisherConfiguration.() -> Unit) {
        taskPublisher = ElasticSearchPublisherConfiguration().also(configuration)
    }

    @JvmName("buildPublisherCustomConfiguration")
    fun buildPublisher(configuration: CustomPublisherConfiguration.() -> Unit) {
        taskPublisher = CustomPublisherConfiguration().also(configuration)
    }


}
