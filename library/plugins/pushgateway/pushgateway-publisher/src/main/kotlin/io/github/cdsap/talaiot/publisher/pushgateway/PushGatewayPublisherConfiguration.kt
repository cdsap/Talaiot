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
class PushGatewayPublisherConfiguration : PublisherConfiguration {

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
    /**
     * By default, task metrics are reported using the format:
     * gradle_task_$TASK_NAME { ... }
     * Where the task name composes the metric name.
     * If you prefer to set the task name as metric label:
     * gradle_task { ... label=$TASK_NAME }
     * you can enable it in the extension with the property
     * taskNameAsLabel = true
     */
    var taskNameAsLabel: Boolean = false
}
