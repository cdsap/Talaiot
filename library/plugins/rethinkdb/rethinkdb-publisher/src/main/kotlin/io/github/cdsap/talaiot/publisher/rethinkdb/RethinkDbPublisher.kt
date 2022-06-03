package io.github.cdsap.talaiot.publisher.rethinkdb

import com.rethinkdb.RethinkDB
import com.rethinkdb.net.Connection
import io.github.cdsap.talaiot.entities.ExecutionReport
import io.github.cdsap.talaiot.logger.LogTracker
import io.github.cdsap.talaiot.metrics.DefaultBuildMetricsProvider
import io.github.cdsap.talaiot.metrics.DefaultTaskDataProvider
import io.github.cdsap.talaiot.publisher.Publisher
import java.net.URL

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
    private val logTracker: LogTracker
) : Publisher, java.io.Serializable {

    private val TAG = "RethinkDbPublisher"

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
        }
        val r = RethinkDB.r

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

            checkDb(conn, rethinkDbPublisherConfiguration.dbName, r)
            if (rethinkDbPublisherConfiguration.publishTaskMetrics) {
                val entries = createTaskEntries(report)
                if (entries.isNotEmpty()) {
                    checkTable(
                        conn,
                        rethinkDbPublisherConfiguration.dbName,
                        rethinkDbPublisherConfiguration.taskTableName,
                        r
                    )
                    insertEntries(
                        conn,
                        rethinkDbPublisherConfiguration.dbName,
                        rethinkDbPublisherConfiguration.taskTableName,
                        entries,
                        r
                    )
                }
            }

            println(rethinkDbPublisherConfiguration.publishBuildMetrics)
            if (rethinkDbPublisherConfiguration.publishBuildMetrics) {
                val entries = DefaultBuildMetricsProvider(report).get()
                if (entries != null && entries.isNotEmpty()) {
                    checkTable(
                        conn,
                        rethinkDbPublisherConfiguration.dbName,
                        rethinkDbPublisherConfiguration.buildTableName,
                        r
                    )
                    insertEntries(
                        conn,
                        rethinkDbPublisherConfiguration.dbName,
                        rethinkDbPublisherConfiguration.buildTableName,
                        entries,
                        r
                    )
                }
            }
        } catch (e: Exception) {
            logTracker.error("RethinkDbPublisher- Error executing the Runnable: ${e.message}")
        }
    }

    private fun insertEntries(
        conn: Connection,
        db: String,
        table: String,
        entries: Map<String, Any>?,
        r: RethinkDB,
    ) {
        r.db(db).table(table).insert(entries).run<Any>(conn)
    }

    private fun checkDb(conn: Connection, db: String, r: RethinkDB) {
        val exist = r.dbList().contains(db).run<Boolean>(conn)
        if (!exist) {
            r.dbCreate(db).run<Any>(conn)
        }
    }

    private fun checkTable(conn: Connection, db: String, table: String, r: RethinkDB) {
        val exist = r.db(db).tableList().contains(table).run<Boolean>(conn)

        if (!exist) {
            try {
                val a = r.db(db).tableCreate(table).run<Any>(conn)
            } catch (e: Exception) {
                println(e.message)
            }
        }
    }

    private fun createTaskEntries(report: ExecutionReport): Map<String, Any> {
        val list = mutableMapOf<String, Any>()
        report.tasks?.forEach { task ->
            list.putAll(DefaultTaskDataProvider(task, report).get())
        }
        return list
    }
}
