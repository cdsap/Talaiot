package com.cdsap.talaiot.publisher

import com.cdsap.talaiot.configuration.InfluxDbPublisherConfiguration
import com.cdsap.talaiot.entities.ExecutionReport
import com.cdsap.talaiot.logger.LogTracker
import com.cdsap.talaiot.request.Request
import okhttp3.OkHttpClient
import org.influxdb.InfluxDB
import org.influxdb.InfluxDBFactory
import org.influxdb.InfluxDBIOException
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
     * Executor to schedule a task in Background
     */
    private val executor: Executor
) : Publisher {

    override fun publish(report: ExecutionReport) {
        logTracker.log("================")
        logTracker.log("InfluxDbPublisher")
        logTracker.log("================")

        if (influxDbPublisherConfiguration.url.isEmpty() ||
            influxDbPublisherConfiguration.dbName.isEmpty() ||
            influxDbPublisherConfiguration.taskMetricName.isEmpty() ||
            influxDbPublisherConfiguration.buildMetricName.isEmpty()
        ) {
            println(
                "InfluxDbPublisher not executed. Configuration requires url, dbName, taskMetricName and buildMetricName: \n" +
                        "influxDbPublisher {\n" +
                        "            dbName = \"tracking\"\n" +
                        "            url = \"http://localhost:8086\"\n" +
                        "            buildMetricName = \"build\"\n" +
                        "            taskMetricName = \"task\"\n" +
                        "}\n" +
                        "Please update your configuration"
            )
            return
        }

        try {
            val _db = createDb()

            val measurements = createTaskPoints(report)
            val buildMeasurement = createBuildPoint(report)

            if (!measurements.isNullOrEmpty()) {
                val points = BatchPoints.builder()
                    .points(measurements)
                    .point(buildMeasurement)
                    //See https://github.com/influxdata/influxdb-java/issues/373
                    .retentionPolicy(influxDbPublisherConfiguration.retentionPolicyConfiguration.name)
                    .build()

                executor.execute {
                    _db.write(points)
                }
            } else {
                logTracker.log("Empty content")
            }
        } catch (e: InfluxDBIOException) {
            logTracker.log("InfluxDb Error: ${e.message}")
        }
    }

    private fun createTaskPoints(report: ExecutionReport): List<Point>? {
        val measurements = report.tasks?.map { task ->
            Point.measurement(influxDbPublisherConfiguration.taskMetricName)
                .time(System.currentTimeMillis(), TimeUnit.MILLISECONDS)
                .tag("state", task.state.name)
                .tag("module", task.module)
                .tag("rootNode", task.rootNode.toString())
                .tag("task", task.taskPath)
                .tag("workerId", task.workerId)
                .tag("critical", task.critical.toString())
                .apply {
                    report.customProperties.taskProperties.forEach { (k, v) ->
                        tag(k, v)
                    }
                }
                .addField("value", task.ms)
                .build()
        }
        return measurements
    }

    private fun createBuildPoint(report: ExecutionReport): Point {
        val buildMeta = report.flattenBuildEnv()
        return Point.measurement(influxDbPublisherConfiguration.buildMetricName)
            .time(report.endMs?.toLong() ?: System.currentTimeMillis(), TimeUnit.MILLISECONDS)
            .apply {
                buildMeta.forEach { (k, v) ->
                    tag(k, v)
                }
            }
            .addField("duration", report.durationMs?.toLong() ?: 0L)
            .addField("configuration", report.configurationDurationMs?.toLong() ?: 0L)
            .addField("success", report.success)
            .apply {
                report.customProperties.buildProperties.forEach { (k, v) ->
                    tag(k, v)
                }
            }
            .apply {
                report.environment.osVersion?.let { addField("osVersion", it) }
                report.environment.maxWorkers?.let { addField("maxWorkers", it.toLong()) }
                report.environment.javaRuntime?.let { addField("javaRuntime", it) }
                report.environment.javaVmName?.let { addField("javaVmName", it) }
                report.environment.javaXmsBytes?.let { addField("javaXmsBytes", it.toLong()) }
                report.environment.javaXmxBytes?.let { addField("javaXmxBytes", it.toLong()) }
                report.environment.javaMaxPermSize?.let { addField("javaMaxPermSize", it.toLong()) }
                report.environment.totalRamAvailableBytes?.let { addField("totalRamAvailableBytes", it.toLong()) }

                report.environment.cpuCount?.let { addField("cpuCount", it.toLong()) }
                report.environment.locale?.let { addField("locale", it) }
                report.environment.username?.let { addField("username", it) }
                report.environment.publicIp?.let { addField("publicIp", it) }
                report.environment.defaultChartset?.let { addField("defaultCharset", it) }
                report.environment.ideVersion?.let { addField("ideVersion", it) }
                report.environment.gradleVersion?.let { addField("gradleVersion", it) }
                report.environment.gitBranch?.let { addField("gitBranch", it) }
                report.environment.gitUser?.let { addField("gitUser", it) }
                report.cacheRatio?.let { addField("cacheRatio", it.toDouble()) }

                report.beginMs?.let { addField("start", it.toDouble()) }
                report.rootProject?.let { addField("rootProject", it) }
                report.requestedTasks?.let { addField("requestedTasks", it) }
                report.scanLink?.let { addField("scanLink", it) }
            }

            .build()
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
