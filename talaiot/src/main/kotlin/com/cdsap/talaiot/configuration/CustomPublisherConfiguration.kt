package com.cdsap.talaiot.configuration

import com.cdsap.talaiot.publisher.Publisher

/**
 * Configuration for a custom [Publisher]. It belongs to the Publisher configurations
 *
 * customPublisher {
 *    publisher = MyCustomPublisher()
 *
 * }
 */
class CustomPublisherConfiguration : PublisherConfiguration {
    override var name = "CustomPublisherConfiguration"
    override var publishBuildMetrics = true
    override var publishTaskMetrics = true
    var publisher: Publisher? = null

}