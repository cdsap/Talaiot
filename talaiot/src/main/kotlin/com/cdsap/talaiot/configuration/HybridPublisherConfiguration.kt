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

}
