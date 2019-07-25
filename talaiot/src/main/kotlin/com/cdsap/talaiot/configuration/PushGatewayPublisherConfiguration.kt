package com.cdsap.talaiot.configuration

/**
 * Configuration for the [com.cdsap.talaiot.publisher.PushGatewayPublisher]
 *
 * pushGatewayPublisher {
 *    url = "http://some.company:8086"
 *    taskJobName = "task"
 *    buildJobName = "build"
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
    var taskJobName: String = "task"
    var buildJobName: String = "build"


}
