package io.github.cdsap.talaiot.publisher.influxdb2

import io.github.cdsap.talaiot.configuration.PublisherConfiguration
import io.github.cdsap.talaiot.metrics.BuildMetrics
import io.github.cdsap.talaiot.metrics.TaskMetrics

/**
 * Configuration for the [InfluxDb2Publisher]. It belongs to the Publisher configurations
 *
 * influxDb2Publisher {
 *    token = System.getEnv($TOKEN)
 *    org = "myOrgName"
 *    bucket = "myBucketName"
 *    buildMetricName = "buildMetric"
 *    taskMetricName = "taskMetric"
 * }
 */
class InfluxDb2PublisherConfiguration : PublisherConfiguration {

    override var publishBuildMetrics: Boolean = true
    override var publishTaskMetrics: Boolean = true

    /**
     * name of the publisher
     */
    override var name: String = "influxDb2"

    /**
     * influx instance auth token
     */
    var token: String = ""

    /**
     * influx instance bucket name
     */
    var bucket: String = ""

    /**
     * influx instance org name
     */
    var org: String = ""

    /**
     * url from the InfluxDb instance required to send the measurements. For instance http://localhost:8086
     */
    var url: String = ""

    /**
     * metric to identify the measurement in InfluxDb
     */
    var taskMetricName: String = "task"

    /**
     * metric name to identify the build measurements in InfluxDb
     */
    var buildMetricName: String = "build"

    /**
     * by default all build metrics are considered fields, specify required [BuildMetrics] to be consider as Tags in InfluxDb
     */
    var buildTags: List<BuildMetrics> = emptyList()

    /**
     * by default all task metrics except execution time are considered tags, specify required [TaskMetrics] to be consider as Tags in InfluxDb
     */
    var taskTags: List<TaskMetrics> = TaskMetrics.values().filter { it != TaskMetrics.Value }
}
