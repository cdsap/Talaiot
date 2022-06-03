package io.github.cdsap.talaiot.publisher.rethinkdb

import io.github.cdsap.talaiot.configuration.PublisherConfiguration

/**
 * Configuration for the [RethinkDbPublisher]. It belongs to the Publisher configurations
 *
 * rethinkDbPublisher {
 *    dbName = "tracking"
 *    url = "url"
 *    taskTableName = "tracking"
 *    buildTableName = "buildMetric"
 *
 * }
 */
class RethinkDbPublisherConfiguration : PublisherConfiguration, java.io.Serializable {

    override var publishBuildMetrics: Boolean = true
    override var publishTaskMetrics: Boolean = true

    /**
     * name of the publisher
     */
    override var name: String = "RethinkDb"
    /**
     * name of the RethinkDb database, will be automatically created
     */
    var dbName: String = ""
    /**
     * url from the RethinkDb instance required to send the measurements.
     */
    var url: String = ""
    /**
     * table to identify the measurement of tasks in RethinkDb
     */
    var taskTableName: String = "task"
    /**
     * table to identify the measurement of builds in RethinkDb
     */
    var buildTableName: String = "build"
    /**
     * optional username for authentication
     */
    var username: String = ""
    /**
     * optional password for authorization
     */
    var password: String = ""
}
