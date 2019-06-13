package com.cdsap.talaiot.configuration

/**
 * Configuration for the PushGatewayPublisher. It belongs to the Publisher configurations
 *
 * pushGatewayPublisher {
 *    url = "url"
 *    jobName = "tracking"
 * }
 */
class PushGatewayPublisherConfiguration : PublisherConfiguration {

    /**
     * name of the publisher
     */
    override var name: String = "pushGateway"
    /**
     * url from the PushGateway instance required to send the measurements. For instance http://localhost:8086
     */
    var url: String = ""
    /**
     * value to identify the job in PushGateway
     */
    var jobName: String = ""


}
