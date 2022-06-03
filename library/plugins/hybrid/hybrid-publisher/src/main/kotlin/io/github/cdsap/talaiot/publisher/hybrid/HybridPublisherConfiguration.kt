package io.github.cdsap.talaiot.publisher.hybrid

import io.github.cdsap.talaiot.configuration.PublisherConfiguration

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
class HybridPublisherConfiguration : PublisherConfiguration, java.io.Serializable {

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
}
