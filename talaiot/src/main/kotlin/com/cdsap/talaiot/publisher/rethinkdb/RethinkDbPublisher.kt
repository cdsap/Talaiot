package com.cdsap.talaiot.publisher.rethinkdb

import com.cdsap.talaiot.configuration.RethinkDbPublisherConfiguration
import com.cdsap.talaiot.entities.*
import com.cdsap.talaiot.logger.LogTracker
import com.cdsap.talaiot.publisher.Publisher
import com.rethinkdb.RethinkDB
import com.rethinkdb.net.Connection
import java.net.URL
import java.util.concurrent.Executor

/**
 * Publisher using RethinkDb format to send the metrics
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

    val r = RethinkDB.r

    override fun publish(report: ExecutionReport) {
        if (rethinkDbPublisherConfiguration.url.isEmpty() ||
            rethinkDbPublisherConfiguration.dbName.isEmpty() ||
            rethinkDbPublisherConfiguration.taskTableName.isEmpty() ||
            rethinkDbPublisherConfiguration.buildTableName.isEmpty()
        ) {
            error(
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



        executor.execute {
            logTracker.log(TAG, "================")
            logTracker.log(TAG, "RethinkDbPublisher")
            logTracker.log(TAG, "publishBuildMetrics: ${rethinkDbPublisherConfiguration.publishBuildMetrics}")
            logTracker.log(TAG, "publishTaskMetrics: ${rethinkDbPublisherConfiguration.publishTaskMetrics}")
            logTracker.log(TAG, "================")


            try {
                val url = URL(rethinkDbPublisherConfiguration.url)
                val conn: Connection = if (rethinkDbPublisherConfiguration.username.isBlank() &&
                    rethinkDbPublisherConfiguration.password.isBlank()
                ) {
                    r.connection()
                        .hostname(url.host)
                        .port(url.port)
                        .connect()
                } else {
                    r.connection()
                        .hostname(url.host)
                        .port(url.port)
                        .user(rethinkDbPublisherConfiguration.username, rethinkDbPublisherConfiguration.password)
                        .connect()
                }

                checkDb(conn, rethinkDbPublisherConfiguration.dbName)

                if (rethinkDbPublisherConfiguration.publishTaskMetrics) {
                    val entries = createTaskEntries(report)
                    if (entries != null && entries.isNotEmpty()) {
                        checkTable(
                            conn,
                            rethinkDbPublisherConfiguration.dbName,
                            rethinkDbPublisherConfiguration.taskTableName
                        )
                        insertEntries(
                            conn,
                            rethinkDbPublisherConfiguration.dbName,
                            rethinkDbPublisherConfiguration.taskTableName,
                            entries
                        )
                    }
                }

                if (rethinkDbPublisherConfiguration.publishBuildMetrics) {
                    val entries = createBuildEntry(report)
                    if (entries != null && entries.isNotEmpty()) {
                        checkTable(
                            conn,
                            rethinkDbPublisherConfiguration.dbName,
                            rethinkDbPublisherConfiguration.buildTableName
                        )
                        insertEntries(
                            conn,
                            rethinkDbPublisherConfiguration.dbName,
                            rethinkDbPublisherConfiguration.buildTableName,
                            entries
                        )
                    }
                }

            } catch (e: Exception) {
                logTracker.error("RethinkDbPublisher- Error executing the Runnable: ${e.message}")
            }
        }
    }

    private fun insertEntries(
        conn: Connection,
        db: String,
        table: String,
        entries: List<Map<String, Any>>?
    ) {
        r.db(db).table(table).insert(entries).run<Any>(conn)
    }

    private fun checkDb(conn: Connection, db: String) {
        val exist = r.dbList().contains(db).run<Boolean>(conn)
        if (!exist) {
            r.dbCreate(db).run<Any>(conn)
        }
    }

    private fun checkTable(conn: Connection, db: String, table: String) {
        val exist = r.db(db).tableList().contains(table).run<Boolean>(conn)
        if (!exist) {
            r.db(db).tableCreate(table).run<Any>(conn)
        }
    }

    private fun createTaskEntries(report: ExecutionReport): List<Map<String, Any>> {
        val taskCustomProperties = getCustomProperties(report.customProperties.taskProperties)
        val taskInfoProperties = report.tasks?.flatMap { task ->
            listOf(
                mapOf<String, Any>(
                    "state" to task.state.name,
                    "module" to task.module,
                    "time" to System.currentTimeMillis(),
                    "rootNode" to task.rootNode.toString(),
                    "task" to task.taskPath,
                    "workerId" to task.workerId,
                    "value" to task.ms,
                    "critical" to task.critical.toString()
                )
            )
        } ?: emptyList()
        return taskCustomProperties + taskInfoProperties
    }

    private fun getCustomProperties(taskProperties: MutableMap<String, String>): List<Map<String, Any>> {
        return taskProperties.flatMap {
            listOf(mapOf(it.key to it.value))
        }
    }

    private fun createBuildEntry(report: ExecutionReport): List<Map<String, Any>>? {
        val buildMeta = report.flattenBuildEnv()
        val buildMetaProperties = getCustomProperties(buildMeta.toMutableMap())
        val buildCustomsProperties = getCustomProperties(report.customProperties.buildProperties)
        val buildEnvironmentProperties = getEnvironment(report)
        val buildInfoProperties = mapOf<String, Any>(
            "time" to (report.endMs?.toLong() ?: System.currentTimeMillis()),
            "duration" to (report.durationMs?.toLong() ?: 0L),
            "configuration" to (report.configurationDurationMs?.toLong() ?: 0L),
            "success" to report.success
        )
        return buildMetaProperties + buildCustomsProperties + buildEnvironmentProperties + buildInfoProperties
    }

    private fun getEnvironment(report: ExecutionReport): Map<String, Any> {
        val map = mutableMapOf<String, Any>()
        report.apply {
            environment.cpuCount?.let { map["cpuCount"] = it.toLong() }
            environment.locale?.let { map["locale"] = it }
            environment.username?.let { map["username"] = it }
            environment.publicIp?.let { map["publicIp"] = it }
            environment.defaultChartset?.let { map["defaultCharset"] = it }
            environment.ideVersion?.let { map["ideVersion"] = it }
            environment.gradleVersion?.let { map["gradleVersion"] = it }
            environment.gitBranch?.let { map["gitBranch"] = it }
            environment.gitUser?.let { map["gitUser"] = it }
        }
        report.apply {
            cacheRatio?.let { map["cacheRatio"] = it.toDouble() }
            beginMs?.let { map["start"] = it.toDouble() }
            rootProject?.let { map["rootProject"] = it }
            requestedTasks?.let { map["requestedTasks"] = it }
            scanLink?.let { map["scanLink"] = it }
        }
        return map
    }
}
