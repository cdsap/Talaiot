package io.github.cdsap.talaiot.plugin.pushgateway

import groovy.lang.Closure
import io.github.cdsap.talaiot.publisher.PublishersConfiguration
import io.github.cdsap.talaiot.publisher.pushgateway.PushGatewayPublisher
import io.github.cdsap.talaiot.publisher.pushgateway.PushGatewayPublisherConfiguration
import org.gradle.api.Project

class PushgatewayConfiguration(project: Project) : PublishersConfiguration(project) {

    var pushGatewayPublisher: PushGatewayPublisherConfiguration? = null

    /**
     * Configuration accessor within the [PublishersConfiguration] for the [PushGatewayPublisher]
     *
     * @param configuration Configuration block for the [PushGatewayPublisherConfiguration]
     */
    fun pushGatewayPublisher(configuration: PushGatewayPublisherConfiguration.() -> Unit) {
        pushGatewayPublisher = PushGatewayPublisherConfiguration().also(configuration)
    }

    /**
     * Configuration accessor within the [PublishersConfiguration] for the [PushGatewayPublisher]
     *
     * @param closure closure for the [PushGatewayPublisherConfiguration]
     */
    fun pushGatewayPublisher(closure: Closure<*>) {
        pushGatewayPublisher = PushGatewayPublisherConfiguration()
        closure.delegate = pushGatewayPublisher
        closure.call()
    }
}
