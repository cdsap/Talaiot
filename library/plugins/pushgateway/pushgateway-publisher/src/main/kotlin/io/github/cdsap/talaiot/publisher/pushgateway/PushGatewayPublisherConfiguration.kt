package io.github.cdsap.talaiot.publisher.pushgateway

import io.github.cdsap.talaiot.configuration.PublisherConfiguration

/**
 * Configuration for the [PushGatewayPublisher]
 *
 * pushGatewayPublisher {
 *    url = "http://some.company:8086"
 *    taskJobName = "task"
 *    buildJobName = "build"
 * }
 */
class PushGatewayPublisherConfiguration : PublisherConfiguration, java.io.Serializable {

    /**
     * name of the publisher
     */
    override var name: String = "pushGateway"
    override var publishBuildMetrics: Boolean = true
    override var publishTaskMetrics: Boolean = true
    /**
     * url from the PushGateway instance required to send the measurements. For instance http://localhost:8086
     */
    var url: String = ""
    /**
     * value to identify the job in PushGateway
     */
    var taskJobName: String = "task"
    var buildJobName: String = "build"
}
