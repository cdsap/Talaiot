package com.cdsap.talaiot.publisher

import com.cdsap.talaiot.configuration.InfluxDbPublisherConfiguration
import com.cdsap.talaiot.entities.AggregatedMeasurements
import com.cdsap.talaiot.logger.LogTracker
import com.cdsap.talaiot.request.Request
import okhttp3.OkHttpClient
import org.influxdb.InfluxDB
import org.influxdb.InfluxDBFactory
import org.influxdb.dto.BatchPoints
import org.influxdb.dto.Point
import java.util.concurrent.Executor
import java.util.concurrent.TimeUnit

const val TIMEOUT_SEC = 60L

/**
 * Publisher using InfluxDb and LineProtocol format to send the metrics
 */
class InfluxDbPublisher(
    /**
     * General configuration for the publisher
     */
    private val influxDbPublisherConfiguration: InfluxDbPublisherConfiguration,
    /**
     * LogTracker to print in console depending on the Mode
     */
    private val logTracker: LogTracker,
    /**
     * Interface to send the measurements to an external service
     */
    private val requestPublisher: Request,
    /**
     * Executor to schedule a task in Background
     */
    private val executor: Executor
) : Publisher {

    override fun publish(measurements: AggregatedMeasurements) {
        logTracker.log("================")
        logTracker.log("InfluxDbPublisher")
        logTracker.log("================")

        if (influxDbPublisherConfiguration.url.isEmpty() ||
            influxDbPublisherConfiguration.dbName.isEmpty() ||
            influxDbPublisherConfiguration.urlMetric.isEmpty()
        ) {
            println(
                "InfluxDbPublisher not executed. Configuration requires url, dbName and urlMetrics: \n" +
                        "influxDbPublisher {\n" +
                        "            dbName = \"tracking\"\n" +
                        "            url = \"http://localhost:8086\"\n" +
                        "            urlMetric = \"tracking\"\n" +
                        "}\n" +
                        "Please update your configuration"
            )
            return
        }

        val _db = createDb()

        val tasks = taskMeasurementAggregated.taskMeasurement
        val meta = taskMeasurementAggregated.values

        val measurements = tasks.map { task ->
            Point.measurement(influxDbPublisherConfiguration.urlMetric)
                .time(System.currentTimeMillis(), TimeUnit.MILLISECONDS)
                .tag("state", task.state.name)
                .tag("module", task.module)
                .tag("rootNode", task.rootNode.toString())
                .tag("task", task.taskPath)
                .apply {
                    meta.forEach {
                        tag(it.key.formatTagPublisher(), it.value.formatTagPublisher())
                    }
                }
                .addField("value", task.ms)
                .build()
        }

        if (measurements.isNotEmpty()) {
            val points = BatchPoints.builder()
                .points(measurements)
                .build()

            executor.execute {
                _db.write(points)
            }
        } else {
            logTracker.log("Empty content")
        }
    }

    private fun createDb(): InfluxDB {
        val okHttpBuilder = OkHttpClient.Builder()
            .connectTimeout(TIMEOUT_SEC, TimeUnit.SECONDS)
            .readTimeout(TIMEOUT_SEC, TimeUnit.SECONDS)
            .writeTimeout(TIMEOUT_SEC, TimeUnit.SECONDS)

        val user = influxDbPublisherConfiguration.username
        val password = influxDbPublisherConfiguration.password
        val url = influxDbPublisherConfiguration.url
        val dbName = influxDbPublisherConfiguration.dbName
        val retentionPolicyConfiguration = influxDbPublisherConfiguration.retentionPolicyConfiguration

        val influxDb = if (user.isNotEmpty() && password.isNotEmpty()) {
            InfluxDBFactory.connect(url, user, password, okHttpBuilder)
        } else {
            InfluxDBFactory.connect(url, okHttpBuilder)
        }
        influxDb.setLogLevel(InfluxDB.LogLevel.BASIC)

        val rpName = retentionPolicyConfiguration.name

        if (!influxDb.databaseExists(dbName)) {
            logTracker.log("Creating db $dbName")
            influxDb.createDatabase(dbName)

            val duration = retentionPolicyConfiguration.duration
            val shardDuration = retentionPolicyConfiguration.shardDuration
            val replicationFactor = retentionPolicyConfiguration.replicationFactor
            val isDefault = retentionPolicyConfiguration.isDefault

            influxDb.createRetentionPolicy(rpName, dbName, duration, shardDuration, replicationFactor, isDefault)
        }

        influxDb.setDatabase(dbName)
        influxDb.setRetentionPolicy(rpName)
        influxDb.enableBatch()
        influxDb.enableGzip()
        return influxDb
    }
}
