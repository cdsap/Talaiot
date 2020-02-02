package com.cdsap.talaiot.publisher.rethinkdb

import com.cdsap.talaiot.configuration.RethinkDbPublisherConfiguration
import com.cdsap.talaiot.entities.ExecutionReport
import com.cdsap.talaiot.logger.LogTracker
import com.cdsap.talaiot.publisher.Publisher
import com.cdsap.talaiot.publisher.TIMEOUT_SEC
import okhttp3.OkHttpClient
import org.influxdb.InfluxDB
import org.influxdb.InfluxDBException
import org.influxdb.InfluxDBFactory
import org.influxdb.InfluxDBIOException
import java.util.concurrent.Executor
import java.util.concurrent.TimeUnit

/**
 * Publisher using RethinkDb and LineProtocol format to send the metrics
 */
class RethinkDbPublisher(
    /**
     * General configuration for the publisher
     */
    private val rethinkDbPublisherConfiguration: RethinkDbPublisherConfiguration,
    /**
     * LogTracker to print in console depending on the Mode
     */
    private val logTracker: LogTracker,
    /**
     * Executor to schedule a task in Background
     */
    private val executor: Executor
) : Publisher {

    private val TAG = "RethinkDbPublisher"

    override fun publish(report: ExecutionReport) {
        if (rethinkDbPublisherConfiguration.url.isEmpty() ||
            rethinkDbPublisherConfiguration.dbName.isEmpty() ||
            rethinkDbPublisherConfiguration.taskTableName.isEmpty() ||
            rethinkDbPublisherConfiguration.buildTableName.isEmpty()
        ) {
            logTracker.error(
                "RethinkDbPublisher not executed. Configuration requires url, dbName, taskTableName and buildTableName: \n" +
                        "rethinkDbPublisher {\n" +
                        "            dbName = \"tracking\"\n" +
                        "            url = \"http://localhost:8086\"\n" +
                        "            buildTableName = \"build\"\n" +
                        "            taskTableName = \"task\"\n" +
                        "}\n" +
                        "Please update your configuration"
            )
            return
        }

        try {


            executor.execute {
                logTracker.log(TAG, "================")
                logTracker.log(TAG, "RethinkDbPublisher")
                logTracker.log(TAG, "publishBuildMetrics: ${rethinkDbPublisherConfiguration.publishBuildMetrics}")
                logTracker.log(TAG, "publishTaskMetrics: ${rethinkDbPublisherConfiguration.publishTaskMetrics}")
                logTracker.log(TAG, "================")

                try {
                    val _db = createDb()
                    if (rethinkDbPublisherConfiguration.publishTaskMetrics) {

                        val measurements = createTaskEntries(report)
                        if (!measurements.isNullOrEmpty()) {

                        }
                    }

                    if (rethinkDbPublisherConfiguration.publishBuildMetrics) {
                        val buildMeasurement = createBuildEntries(report)
                        println(buildMeasurement.lineProtocol())
                        pointsBuilder.point(buildMeasurement)

                    }

                    val points = pointsBuilder.build()
                    logTracker.log(TAG, "Sending points to RethinkDb server ${points.toString()}")
                    _db.write(points)
                } catch (e: Exception) {
                    logTracker.log(TAG, "RethinkDbPublisher-Error-Executor Runnable: ${e.message}")

                }
            }
        } catch (e: Exception) {
            logTracker.log(TAG, "RethinkDbPublisher-Error ${e.stackTrace}")
            when (e) {
                is InfluxDBIOException -> {
                    logTracker.log(TAG, "RethinkDbPublisher-Error-InfluxDBIOException: ${e.message}")
                }
                is InfluxDBException -> {
                    logTracker.log(TAG, "RethinkDbPublisher-Error-InfluxDBException: ${e.message}")
                }
                else -> {
                    logTracker.log(TAG, "RethinkDbPublisher-Error-Exception: ${e.message}")
                }
            }
        }
    }

    private fun createTaskEntries(report: ExecutionReport): List<Map<String, Any>>? =
        report.tasks?.flatMap { task ->
            listOf(
                mapOf(
                    "time" to System.currentTimeMillis(),
                    "state" to task.state.name,
                    "module" to task.module,
                    "rootNode" to task.rootNode.toString(),
                    "task" to task.taskPath,
                    "workerId" to task.workerId,
                    "critical" to task.critical.toString(),
                    //     report.customProperties.taskProperties.flatMap {
                    //         mapOf(it.key to it.value)
                    //     },
                    "value" to task.ms

                )

            )
        }


    private fun createBuildEntry(report: ExecutionReport): Map<String, Any> {
        val buildMeta = report.flattenBuildEnv()
        val a = mapOf<String, Any>(
            "time" to (report.endMs?.toLong() ?: System.currentTimeMillis()),
            //    buildMeta.forEach { (k, v) ->
            //         tag(k, v)
            //     },
            "duration" to (report.durationMs?.toLong() ?: 0L),
            "configuration" to (report.configurationDurationMs?.toLong() ?: 0L),
            "success" to report.success + getEnvironment(report)
            //
            //report.customProperties.buildProperties.forEach { (k, v) ->
            //  tag(k, v)
            //}


        )
        return a

    }


    private fun getEnvironment(report: ExecutionReport): Map<String, Any> {
        val map = mutableMapOf<String, Any>()
        report.environment {
            cpuCount?.let { map["cpuCount"] = it.toLong() }
            environment.locale?.let { map["locale"] = it }
            environment.username?.let { map["username"] = it }
            environment.publicIp?.let { map["publicIp"] = it }
            environment.defaultChartset?.let { map["defaultCharset"] = it }
            environment.ideVersion?.let { map["ideVersion"] = it }
            environment.gradleVersion?.let { map["gradleVersion"] = it }
            environment.gitBranch?.let { map["gitBranch"] = it }
            environment.gitUser?.let { map["gitUser"] = it }
        }
        report {
            cacheRatio?.let { map["cacheRatio"] = it.toDouble()) }
            beginMs?.let { map["start"] = it.toDouble() }
            rootProject?.let { map["rootProject"] = it }
            requestedTasks?.let { map["requestedTasks"] = it }
            scanLink?.let { map["scanLink"] = it }
        }
        return map
    }


    private fun createDb(): InfluxDB {
        val okHttpBuilder = OkHttpClient.Builder()
            .connectTimeout(TIMEOUT_SEC, TimeUnit.SECONDS)
            .readTimeout(TIMEOUT_SEC, TimeUnit.SECONDS)
            .writeTimeout(TIMEOUT_SEC, TimeUnit.SECONDS)

        val user = rethinkDbPublisherConfiguration.username
        val password = rethinkDbPublisherConfiguration.password
        val url = rethinkDbPublisherConfiguration.url
        val dbName = rethinkDbPublisherConfiguration.dbName
        val retentionPolicyConfiguration = rethinkDbPublisherConfiguration.retentionPolicyConfiguration

        val influxDb = if (user.isNotEmpty() && password.isNotEmpty()) {
            InfluxDBFactory.connect(url, user, password, okHttpBuilder)
        } else {
            InfluxDBFactory.connect(url, okHttpBuilder)
        }
        influxDb.setLogLevel(InfluxDB.LogLevel.BASIC)

        val rpName = retentionPolicyConfiguration.name

        if (!influxDb.databaseExists(dbName)) {
            logTracker.log(TAG, "Creating db $dbName")
            influxDb.createDatabase(dbName)

            val duration = retentionPolicyConfiguration.duration
            val shardDuration = retentionPolicyConfiguration.shardDuration
            val replicationFactor = retentionPolicyConfiguration.replicationFactor
            val isDefault = retentionPolicyConfiguration.isDefault

            influxDb.createRetentionPolicy(
                rpName,
                dbName,
                duration,
                shardDuration,
                replicationFactor,
                isDefault
            )
        }

        influxDb.setDatabase(dbName)
        influxDb.setRetentionPolicy(rpName)
        influxDb.enableBatch()
        influxDb.enableGzip()
        return influxDb
    }
}

