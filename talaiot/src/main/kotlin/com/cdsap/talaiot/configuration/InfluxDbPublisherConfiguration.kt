package com.cdsap.talaiot.configuration

/**
 * Configuration for the InfluxDbPublisher. It belongs to the Publisher configurations
 *
 * influxDbPublisher {
 *    dbName = "tracking"
 *    url = "url"
 *    taskMetricName = "tracking"
 *    buildMetricName = "buildMetric"
 *
 * }
 */
class InfluxDbPublisherConfiguration : PublisherConfiguration {

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
    var retentionPolicyConfiguration: RetentionPolicyConfiguration = RetentionPolicyConfiguration.default
}

data class RetentionPolicyConfiguration(
    val name: String,
    val duration: String,
    val shardDuration: String,
    val replicationFactor: Int,
    val isDefault: Boolean
) {
    companion object {
        val default: RetentionPolicyConfiguration = RetentionPolicyConfiguration("rpTalaiot", "30d", "30m", 2, true)
    }
}
