package io.github.cdsap.talaiot.publisher.influxdb

import groovy.lang.Closure
import io.github.cdsap.talaiot.configuration.PublisherConfiguration
import io.github.cdsap.talaiot.metrics.BuildMetrics
import io.github.cdsap.talaiot.metrics.TaskMetrics

/**
 * Configuration for the [InfluxDbPublisher]. It belongs to the Publisher configurations
 *
 * influxDbPublisher {
 *    dbName = "tracking"
 *    url = "url"
 *    taskMetricName = "tracking"
 *    buildMetricName = "buildMetric"
 *
 * }
 */
class InfluxDbPublisherConfiguration : PublisherConfiguration, java.io.Serializable {

    override var publishBuildMetrics: Boolean = true
    override var publishTaskMetrics: Boolean = true

    /**
     * name of the publisher
     */
    override var name: String = "influxDb"

    /**
     * name of the InfluxDb database, will be automatically created
     */
    var dbName: String = ""

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
     * optional username for authentication
     */
    var username: String = ""

    /**
     * optional password for authorization
     */
    var password: String = ""

    /**
     * metrics retention policy. By default it's named as rpTalaiot and duration is 30 days
     */
    var retentionPolicyConfiguration: RetentionPolicyConfiguration = RetentionPolicyConfiguration()

    /**
     * by default all build metrics are considered fields, specify required [BuildMetrics] to be consider as Tags in InfluxDb
     */
    var buildTags: List<BuildMetrics> = emptyList()

    /**
     * by default all task metrics except execution time are considered tags, specify required [TaskMetrics] to be consider as Tags in InfluxDb
     */
    var taskTags: List<TaskMetrics> = TaskMetrics.values().filter { it != TaskMetrics.Value }

    /**
     * Configuration accessor within the [InfluxDbPublisherConfiguration] for the [io.github.cdsap.talaiot.configuration.RetentionPolicyConfiguration]
     *
     * @param configuration Configuration block for the [RetentionPolicyConfiguration]
     */
    fun retentionPolicyConfiguration(configuration: RetentionPolicyConfiguration.() -> Unit) {
        retentionPolicyConfiguration = RetentionPolicyConfiguration().also(configuration)
    }

    /**
     * Configuration accessor within the [InfluxDbPublisherConfiguration] for the [io.github.cdsap.talaiot.configuration.RetentionPolicyConfiguration]
     *
     * @param closure closure for the [RetentionPolicyConfiguration]
     */
    fun retentionPolicyConfiguration(closure: Closure<*>) {
        retentionPolicyConfiguration = RetentionPolicyConfiguration()
        closure.delegate = retentionPolicyConfiguration
        closure.call()
    }
}

data class RetentionPolicyConfiguration(
    var name: String = "rpTalaiot",
    var duration: String = "30d",
    var shardDuration: String = "30m",
    var replicationFactor: Int = 2,
    var isDefault: Boolean = false
) : java.io.Serializable
